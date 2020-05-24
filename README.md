Octopus Authentication Module
============

Octopus Authentication is a high-assurance, passwordless authentication system engineered to address the diverse authentication needs of a real-world, working enterprise.

Octopus Authentication replaces all employee passwords with a strong, password-free authentication mechanism. Our proprietary Windows Credential Provider works in conjunction with standard Active Directory interfaces to seamlessly deliver a stronger, more secure alternative to passwords.

A direct effect of eliminating passwords with the Octopus Authentication Module is a significant increase in security of AD domains, edge devices and remotely accessed services. Moreover, user experience becomes standardized, resulting in enhanced productivity and accessibility, and password management and support costs are dramatically lowered.

As part of the integration with ForgeRock, Secret Double Octopus allows users to choose the orgeRock Authenticator app as an additional multifactor authentication method. 

<img src=".//media/image3.png" style="width:6.52399in;height:3.0625in" />

**Workstation Authentication using ForgeRock MFA with Push Authentication**

<img src=".//media/image4.png" style="width:5.59373in;height:2.7867in" />

**Workstation Authentication using ForgeRock MFA with OTP Authentication**

<a name="Prerequisites">Prerequisites</a>
-------------

Before beginning installation, verify that the following requirements are met:

-   Octopus Authentication Server v4.6 (or higher) is installed and operating with a
    valid enterprise certificate (for User Portal and administrator
    access via HTTPS)

-   Corporate Active Directory Server is operating with Admin rights and
    an AD LDAP root certificate to establish a secure LDAPS connection

-   Corporate domain Windows machines (user PCs) are available

-   Octopus Authentication for Windows is deployed on all
    corporate Windows machines (Windows users)

-   Octopus Authentication for Mac is deployed (Mac users)

-   ForgeRock is installed and enrolled users are operating the mobile 
    ForgeRock Authenticator (Push or OTP)

-   The Octopus Authentication for Windows MSI and MSIUpdater packages
    have been obtained from the Secret Double Octopus team

Octopus Authentication for Windows supports all Windows versions:
Windows 7, 8, 10 and Servers 2008, 2012 and 2016. Required Windows
updates are listed in the table below.

<table>
<thead>
<tr class="header">
<th><strong>OS / Arch</strong></th>
<th><strong>Required Update</strong></th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td>Windows 7 x86</td>
<td><a href="https://www.microsoft.com/en-us/download/details.aspx?id=49077">KB2999226 update from Microsoft</a></td>
</tr>
<tr class="even">
<td>Windows 7 x64</td>
<td><a href="https://www.microsoft.com/en-us/download/details.aspx?id=49077">KB2999226 update from Microsoft</a></td>
</tr>
<tr class="odd">
<td>Windows 8.1</td>
<td>Refer to <a href=#Appendix>Appendix A: Windows 8.1 Registry Update</a> </td>
</tr>
</tbody>
</table>

System Architecture Overview
-------------------

The high-level architecture of the Octopus Authentication Module is shown in the diagram below. Windows/Mac credential providers are used for workstation authentication by Active Directory. 

Any of the following authentication methods can be used:

-   Passwordless + ForgeRock Push Authentication

-   MFA + Password + ForgeRock Push Authentication

-   MFA + Password + ForgeRock OTP

<img src=".//media/image5.png" style="width:6.5in;height:3.62778in" />

**Octopus + ForgeRock Architecture**

Supported Use Cases
-------------------

#### Use Case 1: Authentication to Windows/Mac using the ForgeRock App

<img src=".//media/image6.png" style="width:6.63749in;height:2.91667in" />

**Octopus + ForgeRock Push**

<img src=".//media/image7.png" style="width:6.58333in;height:3.39998in" />

#### Use Case 2: Authentication to Windows/Mac using ForgeRock OTP

<img src=".//media/image8.png" style="width:6.62426in;height:2.73958in" />

**Octopus + ForgeRock Integration (MFA + OTP)**

<img src=".//media/image9.png" style="width:6in;height:4.19231in" />

#### Use Case 3: Authentication to Windows/Mac using Offline OTP

<img src=".//media/image10.png" style="width:6.61243in;height:3.10417in" />

**Octopus + Offline OTP**

<img src=".//media/OfflineOTPFlow.png" style="width:6in;height:4.19231in" />

Configuring the Octopus Management Console
==========================================

Before beginning the installation and deployment process, make sure to complete the
following preparations in the Octopus Management Console:

-   Adding the ForgeRock Authenticator

-   [Integrating the Corporate Active Directory](#IntegratingAD)

-   [Creating the Active Directory Authentication Service](#CreatingADService)

Adding the ForgeRock Authenticator
----------------------------------

Follow the steps below to add ForgeRock to your list of supported third
party authenticators. The data you will need to create this
authenticator is related to your tree or chain configuration in
ForgeRock. For example:

<img src=".//media/image11.png" style="width:5.89017in;height:2.26042in" />

&nbsp;

<img src=".//media/image12.png" style="width:4.44792in;height:3.50844in" />

**To add the ForgeRock authenticator**:

1.  From the Octopus Management Console, open the **System Settings**
    menu and select **Authenticators**.

1.  Click **Add Authenticator** to open the **Add 3<sup>rd</sup> Party
    Authenticator** dialog.

2.  From the **Type** dropdown list, select **FORGEROCK**.

1.  Configure the following settings:

    <table>
    <thead>
    <tr class="header">
    <th><strong>Setting</strong></th>
    <th><strong>Value / Notes</strong></th>
    </tr>
    </thead>
    <tbody>
    <tr class="odd">
    <td>URL</td>
    <td>The access URL for your ForgeRock identity platform, e.g., <em>https://amlb.domain.com/openam</em></td>
    </tr>
    <tr class="even">
    <td>Chain / Tree Node</td>
    <td>Name of the relevant node/chain in the Authentication Tree</td>
    </tr>
    <tr class="odd">
    <td>Realm Path</td>
    <td>Name of the relevant realm in the AM console</td>
    </tr>
    </tbody>
    </table>

    <img src=".//media/image13.png" style="width:2.625in;height:4.06095in" />

1.  To verify your settings, click **Test Connection**.

2.  Click **Add**.

    **Note**: You can add several ForgeRock authenticators pointing to different nodes/chains, and use each one for a different directory of users or OU. Each directory can be assigned to one authenticator for primary authentication and another authenticator for the OTP chain.

Integrating the Corporate Active Directory
------------------------------------------
<a name="IntegratingAD"></a>
Follow the procedure below to integrate your corporate directory with the Octopus Management Console.

**Note**: The procedure uses an Active Directory type example. You can use the same procedure to integrate a ForgeRock directory type by selecting **ForgeRock** in Step 2. The rest of the steps are the same.

**To integrate the corporate Active Directory:**

1.  From the Octopus Management Console, open the **System Settings** menu and select **Directories**.

1.  Click **Add Directory** to open the **Select Directory Type** dialog. **Active Directory** is the default selection.

    If you want directory users to be synced automatically, enable the **Directory Sync** toggle button. When Directory Sync is disabled (as in the example below) you will need to import users manually. (For more information, refer to the Octopus Management Console Admin Guide.)

    <img src=".//media/image14.png" style="width:2.78795in;height:2.57292in" />

1.  Click **Select**.

    The **New** **Directory** page opens.

1.  Configure the following parameters, based on your corporate AD settings:

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

    <img src=".//media/image15.png" style="width:5.69792in;height:3.74139in" />

1.  To verify your settings, click **Test Connection**.

2.  At the bottom of the page, click **Save**.

### Configuring Authentication Policies for the Directory

After integrating your corporate AD, you need to specify instructions about which authentication options and methods are supported for it. The **Policy** tab of the directory settings enables you to configure:

-   **Supported authenticators:** ForgeRock Authenticator can function as a primary or secondary authenticator (or both).

    - **When ForgeRock is a Primary authenticator**, it can work alongside or instead of Octopus Authenticator. If both methods are enabled, users will be able to choose the authenticator they prefer.

    - **When ForgeRock is a Secondary authenticator**, users first authenticate with another authentication method (not ForgeRock). Then, information is sent to ForgeRock for additional authentication. Information that is sent includes user agent, Source IP and the authentication that was used for the primary authenticator.

-   **OTP settings:** To enhance authentication capabilities, Secret Double Octopus provides the option of issuing a one-time password for login. You can enable either or both of these OTP options:

    - **Online OTP:** When enabled, enrolled users can log into Windows, Mac or the User Portal using a one-time password issued by either the Octopus Authenticator or by ForgeRock.

    - **Offline OTP:** When enabled, enrolled users can log into Windows, Mac or the User Portal using a one-time password that is stored locally. These OTPs are supplied by the Octopus Authenticator or by ForgeRock.

**To configure the directory’s authentication policies**:

1.  From the settings of the directory you just added, open the
    **Policy** tab and scroll to the **Authenticators** section.

1.  From the **Additional Authenticator** dropdown list, select the ForgeRock authenticator. Then select the mapping field that the Octopus Authentication Server will send to ForgeRock as the authentication field (Username, Email, Displayname, etc.).  

    Verify that the **Enable Authenticator as Primary** toggle button is enabled.

    <img src=".//media/image16.png" style="width:4.95208in;height:2.00559in" />

1.  If you want ForgeRock to serve as a further authenticator in addition to other authentication methods (Octopus Authenticator, FIDO, etc.), enable the **Enable Authenticator as Secondary** toggle button.

2.  Scroll down to the **One Time Password (OTP)** section.

    a. To activate online OTP, click the **Enable Online OTP** toggle button. Then, select the appropriate authenticator from the **Online Validator** list. (You can specify either the Octopus Authenticator or the ForgeRock TOTP chain as the OTP validator.)

    b. To activate offline OTP, click the **Enable Offline OTP** toggle button. select the appropriate authenticator from the **Offline Validator** list. (You can specify either the Octopus Authenticator or the ForgeRock TOTP chain as the OTP validator.)

    c. In the **OTP Configuration** section, set the parameters of algorithm, number of digits, time period for replacement of the OTP token, and amount of time for which users are allowed to authenticate offline.

    <img src=".//media/OTPSettings.png" style="width:5.46692in;height:3.3125in" />

1.  At the bottom of the **Policy** tab, click **Save** and then publish your changes.

**Note**: When an additional primary authenticator is selected, that authenticator needs to be selected in the Windows MSI Updater as well. This will allow users to choose that primary authentication method on their Windows workstations.
For more information, refer to [Configuring the MSIUpdater Client](#ConfiguringMSI).

Creating the Active Directory Authentication Service
----------------------------------------------------
<a name="CreatingADService"></a>
Follow the steps below to create the required AD service and configure its settings.

**To add and configure the Active Directory Authentication service:**

1.  From the Octopus Management Console, open the **Services** menu and click **Add Service**.

2.  In the **Active Directory Authentication** tile, click **Add**.

    <img src=".//media/image18.png" style="width:5.11516in;height:3.03958in" />

    Then, in the dialog that opens, click **Create**.

    <img src=".//media/image19.png" style="width:4.82292in;height:2.15176in" />

1.  Review the settings in the **General Info** tab. If you make any changes, click **Save**.

    <table>
    <thead>
    <tr class="header">
    <th><strong>Setting</strong></th>
    <th><strong>Value / Notes</strong></th>
    </tr>
    </thead>
    <tbody>
    <tr class="odd">
    <td>Service Name / Issuer</td>
    <td>Change the default value if desired.</td>
    </tr>
    <tr class="even">
    <td>Description</td>
    <td>Enter a brief note about the service if desired.</td>
    </tr>
    <tr class="odd">
    <td>Display Icon</td>
    <td>This icon will be displayed on the Login page for the service. To change the default icon, click and upload the icon of your choice.</td>
    </tr>
    </tbody>
    </table>

    <img src=".//media/image20.png" style="width:3.6033in;height:2.77914in" />

1.  Open the **Parameters** tab. From the credential type that will be sent by the user for the authentication (usually **Username**).

    <img src=".//media/image21.png" style="width:3.98889in;height:2.68047in" />

    Then, click **Save**.

1.  Open the **Sign On** tab and review the following settings:

    <table>
    <tbody>
    <tr class="odd">
    <td>Setting</td>
    <td>Value</td>
    </tr>
    <tr class="even">
    <td>Endpoint URL</td>
    <td>The access URL from the Windows client to the Octopus Authentication Server</td>
    </tr>
    <tr class="odd">
    <td>Service Key</td>
    <td>An identifier for the service that is used. This key is one of the parameters required for the Windows client to connect to the Octopus Authentication Server.</td>
    </tr>
    <tr class="even">
    <td>Custom Message</td>
    <td>This message is displayed on the mobile during the OWA authentication process only. The message for the Windows authentication is part of the Windows client, and is not set in this service.</td>
    </tr>
    <tr class="odd">
    <td>Authentication Token Timeout</td>
    <td>Used as part of the REST API communication. It sets the token timeout for a request from the application to re-authenticate.</td>
    </tr>
    <tr class="even">
    <td>Rest Payload Signing Algorithm</td>
    <td>Method with which the X.509 certificate was generated.</td>
    </tr>
    <tr class="odd">
    <td>X.509 Certificate</td>
    <td>The service’s certificate file, which will be used as part of the Windows Credential provider installation. Click <strong>Download</strong> to download the file.</td>
    </tr>
    <tr class="even">
    <td>Service Metadata</td>
    <td>The METADATA includes all the information needed to update the MSI file using the MSI updater. Click the button to download the XML file.</td>
    </tr>
    </tbody>
    </table>

    <img src=".//media/image22.png" style="width:5.81601in;height:3.89722in" />

1.  At the bottom of the **Sign On** tab, click **Save**.

2.  Open the **Directories** tab and select the directories that will be available for the service.

    <img src=".//media/DirectoriesTab.png" style="width:4.50895in;height:2.44283in" />

1.  Open the **Users & Groups** tab and click **Add**.

    A popup opens, with a list of directories displayed on the left.

1.  For each directory, select the groups and users to be added to the service.

    <img src=".//media/image24.png" style="width:5.89905in;height:2.74071in" alt="A screenshot of a social media post Description automatically generated" />

1.  After making your selections, click **Save** (in the upper right corner) to close the dialog.

    The groups and users you selected are listed in the **Users & Groups** tab.

1.  Click **Save** and then publish your changes.

Windows Client Installation with MSIUpdater
===========================================

MSI is a Microsoft tool that allows easy and silent installation of Octopus Authenticator for Windows on all clients. This installation option is intended for enterprise IT teams and other large-scale deployments.

 The following sections present the processes required for a successful  deployment with MSI:

-   Installing the MSIUpdater Client

-   [Configuring the MSIUpdater Client](#ConfiguringMSI)

-   [MSI Deployment of Octopus Authenticator](#DeployingMSI)

Installing the MSIUpdater Client
--------------------------------

The MSIUpdater client provides an update tool for basic MSI with the Corporate Octopus AD Authentication configuration. This enables MSI silent installation to corporate Windows clients.

MSIUpdater can run on any Windows client running the following versions: Windows 7, 8, 10, Server 2002, 2008, 2010 and 2012.

Before you begin, verify that all system requirements and
prerequisites are met. For details, refer to the [Prerequisites](#Prerequisites) section of this document.

**To install the MSIUpdater client:**

1.  Run **MSIUpdater.exe**

    If the Microsoft .NET Framework is not installed, an installer opens.

    <img src=".//media/image25.png" style="width:3.54167in;height:2.85979in" />

1.  To launch the wizard, click **Install**.

2.  On the **Welcome** page, click **Next**.

    <img src=".//media/image26.png" style="width:3.30758in;height:2.55208in" />

1.  Accept the license agreement, and then click **Next**.

    <img src=".//media/image27.png" style="width:3.26042in;height:2.50377in" />

1.  To begin the installation, click **Install**.

    <img src=".//media/image28.png" style="width:3.34081in;height:2.56597in" />

    A confirmation message is displayed when installation is complete.

1.  To exit the wizard, click **Finish**.

    <img src=".//media/image29.png" style="width:3.51647in;height:2.64583in" />

When you quit the wizard, the MSIUpdater application will automatically launch, allowing you to configure the **Octopus Authentication for Windows.msi** with the corporate Octopus Active Directory Authentication Sign-On details. For more information, refer to Configuring the MSIUpdater Client (below).

Configuring the MSIUpdater Client
--------------------------------------
<a name="ConfiguringMSI"></a>
The MSIUpdater, which launches automatically after you quit the MSIUpdater installer, updates the Octopus Authentication for Windows
(64-bit or 32-bit) MSI file with the corporate Octopus Active Directory Authentication Sign-On details.

Before you begin working with the MSIUpdater, verify that you have access to the following elements. They can be copied or downloaded from the **Sign On** tab of the Active Directory Authentication service that you created in the Octopus Management Console.

-   **Endpoint URL:** Click the Copy icon to copy the URL.

-   **Service Key:** Click **View**. Then, in the popup that opens, click **Copy** to copy the key.

-   **X.509 Certificate:** Click **Download** to download the
    **cert.pem** file.

Alternatively, you can download all the service metadata at once by clicking **SERVICE METADATA**. The metadata will be saved in the **Metadata.xml** file.

<img src=".//media/image30.png" style="width:5.53043in;height:3.70586in" />

**To configure the MSIUpdater application:**

1.  At the top of the **Parameters** tab, click **Browse** and upload the Octopus Authentication for Windows MSI (64-bit or 32-bit) file to be updated.

    <img src=".//media/image31.png" style="width:3.02208in;height:4.17652in" />

1.  Add the service parameters by pasting the **Endpoint URL** and
    **Service Key** values into the appropriate fields. Then, click
    **Browse** and upload the X.509 certificate file.

    Alternatively, you may upload the parameters by clicking **Load from XML** and uploading the **Metadata.xml** file from the Active Directory Authentication service.

    <img src=".//media/image32.png" style="width:3.30757in;height:2.18819in" />

1.  If you want to use multi-factor authentication from Active Directory when logging into Windows, select the **Multi-Factor Authentication (MFA)** checkbox. When MFA is activated, users will need to enter their AD passwords in order to receive a push notification from Octopus, ForgeRock or OKTA Authenticators. If the checkbox is *not* selected, users will have passwordless authentication.

    >**Note: In order to successfully use a FIDO key with MFA, the key must not have an associated PIN**.

1.  Select one or more of the following authenticators:

    <table>
    <thead>
    <tr class="header">
    <th><strong>Authenticator</strong></th>
    <th><strong>Description</strong></th>
    </tr>
    </thead>
    <tbody>
    <tr class="odd">
    <td>Octopus App</td>
    <td>Octopus Authenticator mobile app (iOS/Android)</td>
    </tr>
    <tr class="even">
    <td>FIDO2</td>
    <td>FIDO authenticator from Yubico</td>
    </tr>
    <tr class="odd">
    <td>OKTA Verify</td>
    <td>For OKTA users using OKTA Verify Authenticator</td>
    </tr>
    <tr class="even">
    <td>ForgeRock</td>
    <td>For ForgeRock users using ForgeRock Authenticator</td>
    </tr>
    <tr class="odd">
    <td>OTP</td>
    <td>This option enables authentication with ForgeRock OTP or Octopus-generated OTP. In order to select the checkbox, the <b>Multi-Factor Authentication (MFA)</b> checkbox must be selected.</td>
    </tr>
    </tbody>
    </table>

    <img src=".//media/image33.png" style="width:3.59031in;height:1.9375in" />

1.  Click **Next**.

2.  On the **Settings** tab, enable the following options as required by selecting the relevant checkboxes:

    <table>
    <tbody>
    <tr class="odd">
    <td>Setting</td>
    <td>Description / Notes</td>
    </tr>
    <tr class="even">
    <td>Lock Screen Authenticate</td>
    <td>Determines whether there is Auto Login for AD users from the Lock screen. When the setting is enabled, AD users receive a push notification from Octopus, ForgeRock or OKTA Authenticators immediately after pressing <strong>&lt;Ctrl&gt; &lt;Alt&gt; &lt;Del&gt;</strong>.</td>
    </tr>
    <tr class="odd">
    <td>Show Default Credential Provider</td>
    <td>Determines whether the entities that control the account options are displayed when logging into Windows. Default credential providers are Windows and Active Directory.</td>
    </tr>
    <tr class="even">
    <td>Enforce MFA</td>
    <td>When selected, users must authenticate with mobile (2<sup>nd</sup> factor) when using domain username and password. This setting is relevant for users with Octopus, ForgeRock or OKTA authenticators only (not FIDO).</td>
    </tr>
    <tr class="odd">
    <td>SafeGuard Support</td>
    <td>Selecting this option enables Octopus Authentication for Windows to login to the SafeGuard client (session).</td>
    </tr>
    <tr class="even">
    <td>TPM Support</td>
    <td>If TPM 2.0 is enabled, selecting this option allows TPM to store the private key for BLE password encryption.</td>
    </tr>
    <tr class="odd">
    <td>Change Password on RDP</td>
    <td>When selected, password changes on RDP sessions are allowed. This option is relevant mainly for admin users using RDP sessions that do not login to Windows machines. This option is relevant to passwordless authentication only.</td>
    </tr>
    <tr class="even">
    <td>Change Password on Unlock</td>
    <td>When selected, password changes are allowed on Unlock as well as on Login to the workstation. This option is relevant to passwordless authentication only.</td>
    </tr>
    <tr class="odd">
    <td>POC Mode</td>
    <td>When selected, Octopus Authentication for Windows will not check the certificate with the server. This setting is used mainly for POC, when using a self-signed certificate on the Octopus Authentication Server.</td>
    </tr>
    <tr class="even">
    <td>Local User Support</td>
    <td>When selected, Octopus Authentication for Windows will be enabled for local users and will verify that the local user matches the mapping with Octopus Authentication Server user.</td>
    </tr>
    <tr class="odd">
    <td>Bypass MFA on unlock when connected to AD</td>
    <td>When selected, users connected to the enterprise network who have already authenticated with MFA are not required to authenticate with 2<sup>nd</sup> factor again when unlocking the workstation. This behavior continues for an unlimited period of time, provided that users remain inside the network.</td>
    </tr>
    <tr class="even">
    <td>Force Lock After Offline OTP</td>
    <td>When selected, workstations that were unlocked using an Offline OTP and then connected back to enterprise network (online) are automatically locked and the user is asked to authenticate. This setting prevents users from using weak authentication to log into the enterprise network (online).</td>
    </tr>
    </tbody>
    </table>

    <img src=".//media/image34.png" style="width:2.82452in;height:4.1125in" />

1.  Configure the following settings, as required:

    <table>
    <thead>
    <tr class="header">
    <th><strong>Setting</strong></th>
    <th><strong>Description / Notes</strong></th>
    </tr>
    </thead>
    <tbody>
      <tr class="even">
    <td>Enable SSO / Enable Third Party SSO</td>
    <td><b>You may configure ONE of these settings only.</b> After selecting the checkbox, enter the portal URL / 3rd party portal URL. In runtime, the portal will open in the default browser. Users will be automatically logged in and be able to view all assigned services.</td>
    </tr>
    <tr class="odd">
    <td>Enable CP Bypass List</td>
    <td>Displays a list of credential providers that will not be filtered by Octopus Authentication for Windows.</td>
    </tr>
    <tr class="even">
    <td>Enable Change OTP Name</td>
    <td>Allows you to change the default name of the OTP displayed in the Windows credential provider’s login authentication method selection list. After selecting the checkbox, enter the desired name in the field (e.g., <em>ForgeRock OTP</em>).</td>
    </tr>
    </tbody>
    </table>

1.  At the bottom of the **Settings** tab, click **Apply**.

    A new modified MSI file is created in the same location as the original MSI file. The name of the new file will include Octopus Authentication for Windows 32-bit or 64-bit and the timestamp of file creation.

    >**Note:** The original MSI file will not be updated and can be reused. **Do not use the original MSI file for installation**.

MSI Deployment of Octopus Authenticator
---------------------------------------
<a name="DeployingMSI"></a>
The following sections explain how to deploy and upgrade using the MSI tool.

### Performing Silent Installation

Silent installation allows administrators to push the installation to
all client machines from a central tool (e.g., GPO). All required
components are installed as part of the deployment.

>**Note:** Administrator permissions are required to run the Octopus Authentication for Windows MSI.

**To perform silent installation:**

1.  Open and run your distributing software.

1.  Open the command prompt as Admin, and run  
    *Octopus Authentication for Windows (64bit or 32bit).msi*

2.  Run *Octopus Authentication for windowsxx.msi /qn*:

    -    Windows 64-bit:  
        *C:\\&gt; Octopus Authentication for Windows64bit – xx\_xxx\_xx.msi
        /qn*

    -    Windows 32-bit:  
        *C:\\&gt; Octopus Authentication for Windows32bit – xx\_xxx\_xx.msi
        /qn*

### Performing MSI Upgrade

**IMPORTANT**: To successfully perform MSI upgrade, the MSI file must have the same filename as that of the original installation. The MSI updater creates an MSI file with the update date in the filename. **This file needs to be renamed to match the name of the original installation file**.

If you try to upgrade using an MSI file that is named differently from the original installation file, the following error message will appear: *Error 1316: The specified account already exists*

This error is an alert that you are trying to install an MSI file that has a different name from the one that is already installed.



If you are not sure of the name of the original installation file, follow these steps:

1. Navigate to **C:\Windows\Installer**

1. Open the following file: **SourceHash{F88FAA40-72B9-4CE0-88DA-6592EF361C94}**

2. Search for the name of the file that was used for installation. You will find it at the end of the SourceHash file.

To upgrade the MSI, run the following command:

-   Windows 64-bit:  
    *C:\\&gt; msiexec /I " Octopus Authentication For Windows 64bit.msi" REINSTALL=ALL REINSTALLMODE=vomus IS_MINOR_UPGRADE=1 /qn*

-   Windows 32-bit:  
    *C:\\&gt; msiexec /I " Octopus Authentication For Windows 32bit.msi" REINSTALL=ALL REINSTALLMODE=vomus IS_MINOR_UPGRADE=1 /qn*

Windows ForgeRock Authentication
================================

Once installation is complete, the user can authenticate to a Windows machine using ForgeRock or a FIDO key (according to the configured
setup). The different authentication flows are described below.

<img src=".//media/image3.png" style="width:5.63542in;height:2.64539in" />

#### ForgeRock MFA Authentication Flow (with ForgeRock Push):

1. User selects ForgeRock as the authentication method.

1. User enters username + Password.

2. User receives a push notification to the mobile device (ForgeRock App) with a prompt to approve authentication.

3. Once authentication is approved, the user is logged into Windows.

      <img src=".//media/image4.png" style="width:6.25188in;height:3.11458in" />

#### ForgeRock MFA Authentication Flow (with ForgeRock OTP):

1. User selects ForgeRock OTP as the authentication method.

1. User enter username + Password + OTP.

2. Once the system verifies the credentials, the user is logged into Windows.

Appendix A: Windows 8.1 Registry Update
================================

<a name="Appendix"/> Follow the steps below to change the ownership of the relevant Credential Providers registry key from *TrustInstaller* to *Domain Admins*.


**To update the registry key ownership:**

1.  Connect to the machine registry and navigate to:
    *[HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Authentication\Credential Providers\{60b78e88-ead8-445c-9cfd-0b87f74ea6cd}]*

2. Right-click on the registry key and select **Permissions**.

    <img src=".//media/RegistryKey_1.png" style="width:3.30758in;height:2.55208in" />
    
    Then click **Advanced** to open the **Advanced Security Settings** dialog.
    
1.  The *Owner* value should appear at the top of the dialog. Click **Change** and set the ownership to *Domain Admins*.

    <img src=".//media/RegistryKey_2.png" style="width:3.30758in;height:2.55208in" />


Octopus Support
================================

For technical assistance please contact us at support@doubleoctopus.com
