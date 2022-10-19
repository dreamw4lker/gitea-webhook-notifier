# Gitea webhook notifier

## About
This project can be used to notify about push events in any Gitea repositories.
There are no built-in functionality in Gitea, as provided [in this gitea issue](https://github.com/go-gitea/gitea/issues/8365).

It is based on Gitea webhook events.

## Build

Build instruments:
* Java 11
* Maven

Run command from root project directory: ``mvn clean install``

Result JAR file: `<root project dir>/target/gitea-webhook-notifier-<version>.jar`

## Launch and maintenance

Use **application.properties** configuration file to set up server and mail sender properly. Configuration file must contain:
```
# Service port
server.port=7700

# Base logging methods
logging.level.org.springframework.web=INFO

# Mail params
mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory
mail.smtp.socketFactory.port=465
mail.smtp.host=smtp.mailhost.somedomain
mail.smtp.auth=true
mail.smtp.port=465
mail.username=mailuser@mailhost.somedomain
mail.password=password
mail.from=mailuser@mailhost.somedomain
```

To launch server you must place **application.properties** file in the same folder as JAR and run command using **Java 11+**: 

`java -jar gitea-webhook-notifier-<version>.jar`

If you want to place configuration file outside JAR directory, use command: 

`java -jar gitea-webhook-notifier-<version>.jar --spring.config.location=<...some path>/application.properties`

Now the service will be available at http://localhost:7700

## Gitea setup

Now you need to set up Gitea webhooks for your project. 

* Make sure that this service is available from the Gitea server
* Open your repository in browser and click on `Settings` link in top-right corner
Move to `Webhooks`, press `Add Webhook -> Gitea`:
![step1](https://user-images.githubusercontent.com/10252565/196620304-f0422385-176d-463f-b6bd-9062a04e639d.png)
* Set up `Target URL`: use href `http://<service url>:<service port>/webhook/handle?r=<comma-separated mail recipients>`:
![step2](https://user-images.githubusercontent.com/10252565/196620365-97ac2024-25d2-4627-9273-51fa3d2b3dc6.png)
* Set up `Trigger On -> Push Events`
* Save your webhook
* Now you can push commit into your repository or use `Test Delivery` at webhook setup page. 
If everything is OK, all recipients from comma-separated list should receive an E-mail
