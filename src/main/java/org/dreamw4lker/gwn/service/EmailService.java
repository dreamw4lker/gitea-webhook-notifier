package org.dreamw4lker.gwn.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dreamw4lker.gwn.domain.WebhookBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

import static org.dreamw4lker.gwn.utils.HtmlUtils.prepareHtmlMessage;
import static org.dreamw4lker.gwn.utils.HtmlUtils.prepareHtmlSubject;

@Slf4j
@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class EmailService {

    private final ConfigurationService configurationService;

    private Session prepareSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", configurationService.getMailSmtpHost());
        props.put("mail.smtp.socketFactory.port", configurationService.getMailSmtpSocketFactoryPort());
        props.put("mail.smtp.socketFactory.class", configurationService.getMailSmtpSocketFactoryClass());
        props.put("mail.smtp.auth", configurationService.getMailSmtpAuth());
        props.put("mail.smtp.port", configurationService.getMailSmtpPort());
        return Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        configurationService.getMailUsername(),
                        configurationService.getMailPassword()
                );
            }
        });
    }

    private Message prepareMessage(Session session, WebhookBean webhookBean, String recipients) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(configurationService.getMailFrom()));
        message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(recipients));
        message.setSubject(prepareHtmlSubject(webhookBean));

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(prepareHtmlMessage(webhookBean), "text/html; charset=utf-8");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);
        return message;
    }

    public void sendSingleMessage(WebhookBean webhookBean, String recipients) throws MessagingException {
        Session session = prepareSession();
        Message message = prepareMessage(session, webhookBean, recipients);
        Transport.send(message);
    }
}
