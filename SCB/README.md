# Octopus Authentication Node

Octopus Authentication is a high-assurance, passwordless authentication
system engineered to address the diverse authentication needs of a
real-world, working enterprise. The Secret Double Octopus solution
replaces all employee passwords with a strong, password-free
authentication mechanism.

Octopus Authentication Node enhances Forgerock’s authentication
capabilities by allowing users to authenticate using the Octopus
Authenticator mobile app. The authentication flow is presented in the
diagram below.

Octopus Authentication Node can be easily downloaded directly from
ForgeRock Marketplace.

![](.//media/image1.png)

**ForgeRock Authentication with Octopus Authentication Node**

## Prerequisites

Before beginning installation, verify that the following requirements
have been met:

  - Octopus Authentication Server v4.6 (or higher) is installed and
    operating with a valid enterprise certificate (for User Portal and
    administrator access via HTTPS)

  - Users are enrolled on the Octopus Authenticator mobile app

  - ForgeRock is installed and operating

  - Octopus Authentication Node has been downloaded from ForgeRock
    Marketplace

## <a name="architecture"></a>System Architecture Overview

The high-level architecture of the Octopus Authentication Node and
ForgeRock integration is shown in the diagram below. The numbers in the
diagram represent the authentication flow:

1.  The user sends a login request to the ForgeRock portal.

2.  The request goes to the ForgeRock server, which operates Octopus
    Authentication Node.

3.  Octopus Authentication Node sends a REST API call to the Octopus
    Server.

4.  The user is authenticated with Octopus Authenticator.

![](.//media/image2.png)

**Octopus Authentication Node and ForgeRock Integration: Architecture and
Flow**

# Configuring the Octopus Management Console

Before installation and configuring Octopus Authentication Node, you
need to make the following preparations in the Octopus Management
Console:

  - Integrating the Corporate Active Directory

  - [Creating the REST API Service](#service)

## Integrating the Corporate Active Directory

Follow the steps below to integrate your corporate directory with
the Octopus Management Console.

**Note:** The procedure uses an Active Directory type example. You can
use the same procedure to integrate a ForgeRock directory type by
selecting **ForgeRock** in Step 2. The rest of the steps are the same.

**To integrate the corporate Active Directory:**

1.  From the Octopus Management Console, open the **System Settings**
    menu and select **Directories**.
5.  Click **Add Directory** to open the **Select Directory Type**
    dialog. **Active Directory** is the default selection.

    If you want directory users to be synced automatically, enable the **Directory Sync** toggle button. When Directory Sync is disabled (as in the example below) you will need to import users manually. (For more information, refer to the Octopus Management Console Admin Guide.)

    ![](.//media/image3.png)

6.  Click **Select**.

    The **New** **Directory** page opens.

7.  Configure the following parameters, based on your corporate AD
    settings:

    <table>
<thead>
<tr class="header">
<th><strong>Parameter</strong></th>
<th><strong>Value / Notes</strong></th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td>Name</td>
<td>Corporate Active Directory Server name</td>
</tr>
<tr class="even">
<td>Base DN</td>
<td>Active Directory Distinguished Name; Active Directory top tree level, from where a server will search for users<br />
(e.g., dc=&lt;AD name&gt;,dc=com)</td>
</tr>
<tr class="odd">
<td>User DN</td>
<td>Active Directory Administrator User DN string (e.g., cn=administrator=users, dc=&lt;AD name&gt;,dc=com)</td>
</tr>
<tr class="even">
<td>Password</td>
<td>Active Directory Administrator Principal’s password</td>
</tr>
<tr class="odd">
<td>Host Name/URL</td>
<td>Corporate Active Directory URL (LDAP/LDAPS) and port</td>
</tr>
<tr class="even">
<td>Upload Certificate</td>
<td>Active Directory LDAPS 64-base encoded root CA. If you are using LDAPS, click and upload the certificate file.</td>
</tr>
</tbody>
</table>

    ![](.//media/image4.png)

8.  To verify your settings, click **Test Connection**.

9.  At the bottom of the page, click **Save**.

10. Open the directory’s **Policy** tab. In the **Authenticators**
    section, verify that the **Octopus Authenticator** toggle button is
    enabled.

    ![](.//media/image5.png)

11. Click **Save** (if the **Save** button is enabled) and then publish
    your changes.

## <a name="service"></a>Creating the REST API Service

Follow the steps below to create the required REST API service and
configure its settings.

**To add and configure the REST API service:**

1.  From the Octopus Management Console, open the **Services** menu and
    click **Add Service**.

12. In the **REST API** tile, click **Add**.

    ![](.//media/image6.png)

    Then, in the dialog that opens, click **Create**.

    ![](.//media/image7.png)

13. Review the settings in the **General Info** tab. If you make any
    changes, click **Save**.

    | **Setting**           | **Value / Notes**                                                                                                                    |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------------ |
| Service Name / Issuer | Change the default value if desired.                                                                                                 |
| Description           | Enter a brief note about the service if desired.                                                                                     |
| Display Icon          | This icon will be displayed on the Login page for the service. To change the default icon, click and upload the icon of your choice. |

    ![](.//media/image8.png)

14. Open the **Parameters** tab. From the **Octopus Authentication
    Login** dropdown list, select the credential type that will be sent
    by the user for the authentication (usually **Email**).

    ![](.//media/image9.png)

    Then, click **Save**.

15. Open the **Sign On** tab and review the following settings:

    |       **Setting**                       |            **Value / Notes**                                                                                                                                                                  
| ------------------------------ |                                                                                                                          |
| Authentication Token Timeout   | Used as part of the REST API communication. It sets the token timeout for an authentication request from the application.                            |
| Rest Payload Signing Algorithm | Method with which the X.509 certificate was generated.                                                                                               |
| X.509 Certificate              | The service’s certificate file, which will be used as part of the Windows Credential provider installation. Click **Download** to download the file. |
| REST Endpoint URL              | Access URL from the Windows client to the Octopus Authentication Server.                                                                             |
| Service Key                    | An identifier for the service. This key is one of the parameters required for the Windows client to connect to the Octopus Authentication Server.    |

    ![](.//media/image10.png)

16. At the bottom of the **Sign On** tab, click **Save**.

17. Open the **Directories** tab and select the directories that will be
    available for the service. Then, click **Save**.

    ![](.//media/image11.png)

18. Open the **Users & Groups** tab and click **Add**.

    A popup opens, with a list of directories displayed on the left.

19. For each directory, select the groups and users to be added to the
    service.

    After making your selections, click **Save** (in the upper right corner)
to close the dialog.

    The groups and users you selected are listed in the **Users & Groups**
tab.

    ![](.//media/image12.png)

20. Click **Save** and then publish your changes.

# Installing and Configuring Octopus Authentication Node

Octopus Authentication Node enables authentication to ForgeRock using
Octopus Authenticator. When the node is added to your realm flow,
authentication requests are sent to the Octopus Authentication Server,
and users then approve the authentication on the Octopus Authenticator
Mobile App (see [System Architecture](#architecture) diagram).

**IMPORTANT**: In order to successfully work with Octopus Authentication
Node, the Octopus Authentication Server needs to be installed and
operational, and the relevant users need to be enrolled with the Octopus
Authenticator Mobile App.

**To install Octopus Authentication Node:**

1.  Download the latest version of the SDO integration
    (**octopusNode-1.0.0.jar**) from ForgeRock MarketPlace.

21. Copy the .jar file into the
    **../web-container/webapps/openam/WEB-INF/lib** directory where your
    AM console is deployed.

22. Restart the web container to pick up the new node.

    After the restart, the node will appear in the Authentication Trees **Components** palette.

23. Add Octopus Authentication Node to your environment. For example:

    ![](.//media/image13.png)

24. Continue by configuring node parameters, as described below.

## Configuring Octopus Authentication Node

The final step of the integration process is configuring the parameters
for Octopus Authentication Node.

![](.//media/image14.png)

The values for the **Service Key**, **Service URL** and **Service
Certificate** can be copied from the **Sign On** tab of the REST API
service that you created in the Octopus Management Console:

  - **REST Endpoint URL:** Click the Copy icon to copy the value.

  - **X.509 Certificate and Service Key:** Click **View**. Then, in the
    popup that opens, click **Copy** to copy the value.

![](.//media/image15.png)
