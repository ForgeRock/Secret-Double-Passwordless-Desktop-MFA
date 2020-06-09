/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 * Copyright 2017-2018 ForgeRock AS.
 */

package org.octopus.octopusNode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Base64;

import javax.inject.Inject;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.forgerock.openam.annotations.sm.Attribute;
import org.forgerock.openam.auth.node.api.AbstractDecisionNode;
import org.forgerock.openam.auth.node.api.Action;
import org.forgerock.openam.auth.node.api.Node;
import org.forgerock.openam.auth.node.api.SharedStateConstants;
import org.forgerock.openam.auth.node.api.TreeContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.assistedinject.Assisted;

@Node.Metadata(outcomeProvider = AbstractDecisionNode.OutcomeProvider.class, configClass = octopusNode.Config.class)
public class octopusNode extends AbstractDecisionNode {
    private final Logger logger = LoggerFactory.getLogger("amAuth");
    private final String serviceKey;
    private final String serviceUrl;
    private PublicKey publicKey;

    /**
     * Configuration for the Octopus node.
     */
    public interface Config {
        @Attribute(order = 100)
        default String serviceKey() {
            return "";
        }

        @Attribute(order = 200)
        default String serviceUrl() {
            return "";
        }

        @Attribute(order = 300)
        default String serviceCert() {
            return "";
        }
    }

    @Inject
    public octopusNode(@Assisted final Config config) throws CertificateException {
        this.serviceKey = config.serviceKey();
        this.serviceUrl = config.serviceUrl();
        String serviceCert = config.serviceCert();
        StringBuilder builder = new StringBuilder(serviceCert);
        int first = builder.indexOf(" ");
        int last = builder.lastIndexOf(" ");
        builder.setCharAt(first, '#');
        builder.setCharAt(last, '#');
        String formatted = builder.toString();
        formatted = formatted.replaceAll(" ", "\n");
        formatted = formatted.replaceAll("#", " ");
        //TODO prefer to use debug for these outputs
        logger.error(formatted);
        //TODO this should be abstracted out and stored in a singleton (this constructor is called every time this node in invoked)
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream targetStream = new ByteArrayInputStream(formatted.getBytes());
        Certificate cert = cf.generateCertificate(targetStream);
        this.publicKey = cert.getPublicKey();
    }

    @Override
    public Action process(final TreeContext context) {
        //TODO prefer to use debug for these outputs
        logger.error("process called");
        final String username = context.sharedState.get(SharedStateConstants.USERNAME).asString().toLowerCase();
        //TODO prefer to use debug for these outputs
        logger.error("process username: " + username);
        try {
            return goTo(octopusAuth(username)).build();
        } catch (final Exception e) {
            logger.error("exception:" + e);
            return goTo(false).build();
        }
    }

    private boolean octopusAuth(final String username) throws Exception {
        final RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        final CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();

        final String authToken = preauthRequest(client);
        logger.error("authToken: " + authToken);
        final String authStatus = authRequest(client, authToken, username, "");
        client.close();
        return authStatus.equals("accept");
    }

    private String preauthRequest(final CloseableHttpClient client) throws Exception {
        //TODO Recommend using ForgeRock's built in HTTP client so that you get built in connection pooling
        final HttpPost httpPost = new HttpPost(this.serviceUrl + "/1/preauth");
        httpPost.addHeader("Content-Type", "application/json");
        final String requestBody = "{\"serviceKey\": \"" + this.serviceKey + "\"}";
        final StringEntity entity = new StringEntity(requestBody);
        httpPost.setEntity(entity);
        final CloseableHttpResponse response = client.execute(httpPost);
        final String stringBody = EntityUtils.toString(response.getEntity());

        logger.error("preauth response string: " + stringBody);

        final JsonParser jsonParser = new JsonParser();
        final JsonElement responseObj = jsonParser.parse(stringBody);
        final JsonObject jsonObject = responseObj.getAsJsonObject();
        final JsonElement b64payload = jsonObject.get("payload");
        final String payload = b64payload.getAsString();
        // logger.error("base64 payload: " + payload);

        final JsonElement signatureElement = jsonObject.get("signature");
        final String signature = signatureElement.getAsString();

        final JsonElement algorithmElement = jsonObject.get("algorithm");
        final String algorithm = algorithmElement.getAsString();

        boolean sigResult = checkSignature(payload, signature, algorithm);
        //TODO prefer to use debug for these outputs
        logger.error("preauth signature verification result: " + sigResult);
        if (!sigResult) {
            throw new Exception("preauth invalid signature");
        }

        final byte[] decoded = Base64.getDecoder().decode(payload);
        final String decodedString = new String(decoded, StandardCharsets.UTF_8);

        // logger.error("decoded payload: " + decodedString);

        final JsonElement preauthTree = jsonParser.parse(decodedString);
        final JsonObject preauthObjString = preauthTree.getAsJsonObject();
        final JsonElement authTokenElement = preauthObjString.get("authToken");

        return authTokenElement.getAsString();
    }

    //TODO Password not used
    private String authRequest(final CloseableHttpClient client, final String authToken, final String username,
            final String password) throws Exception {

        final HttpPost httpPost = new HttpPost(this.serviceUrl + "/1/auth");
        httpPost.addHeader("Content-Type", "application/json");
        String authJson = "{" +
                "\"authToken\": \"" + authToken + "\"," +
                "\"username\": \"" + username + "\"," +
                //TODO message should be configurable via a ForgeRock Configuration
                "\"message\": \"hello from plugin\"" +
                "}";
        httpPost.setEntity(new StringEntity(authJson));
        final CloseableHttpResponse response = client.execute(httpPost);
        final String stringBody = EntityUtils.toString(response.getEntity());

        // logger.error("auth response string: " + stringBody);

        //TODO Duplicated code, extract out as method
        final JsonParser jsonParser = new JsonParser();
        final JsonElement responseObj = jsonParser.parse(stringBody);
        final JsonObject jsonObject = responseObj.getAsJsonObject();
        final JsonElement b64payload = jsonObject.get("payload");
        final String payload = b64payload.getAsString();

        // logger.error("base64 payload: " + payload);

        final JsonElement signatureElement = jsonObject.get("signature");
        final String signature = signatureElement.getAsString();

        final JsonElement algoElement = jsonObject.get("algorithm");
        final String algorithm = algoElement.getAsString();

        boolean sigResult = checkSignature(payload, signature, algorithm);
        //TODO prefer to use debug for these outputs
        logger.error("auth signature verification result: " + sigResult);
        if (!sigResult) {
            throw new Exception("auth invalid signature");
        }

        final byte[] decoded = Base64.getDecoder().decode(payload);
        final String decodedString = new String(decoded, StandardCharsets.UTF_8);

        // logger.error("decoded payload: " + decodedString);

        final JsonElement authTree = jsonParser.parse(decodedString);
        final JsonObject authObjString = authTree.getAsJsonObject();
        final JsonElement authStatusElement = authObjString.get("authStatus");

        return authStatusElement.getAsString();
    }

    private boolean checkSignature(String payload, String sig, String algorithm)
            throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        String algoString = "SHA1withRSA";
        if (algorithm.equals("sha256")) {
            algoString = "SHA256withRSA";
        }
        Signature signature = Signature.getInstance(algoString);         
        signature.initVerify(this.publicKey);
        signature.update(payload.getBytes());
        final byte[] decoded = Base64.getDecoder().decode(sig);
        return signature.verify(decoded);
    }
}
