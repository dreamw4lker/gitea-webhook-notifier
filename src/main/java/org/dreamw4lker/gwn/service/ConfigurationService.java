package org.dreamw4lker.gwn.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {
    @Value("${mail.smtp.socketFactory.class}")
    @Getter
    private String mailSmtpSocketFactoryClass;

    @Value("${mail.smtp.socketFactory.port}")
    @Getter
    private String mailSmtpSocketFactoryPort;

    @Value("${mail.smtp.host}")
    @Getter
    private String mailSmtpHost;

    @Value("${mail.smtp.port}")
    @Getter
    private int mailSmtpPort;

    @Value("${mail.smtp.auth}")
    @Getter
    private String mailSmtpAuth;

    @Value("${mail.username}")
    @Getter
    private String mailUsername;

    @Value("${mail.password}")
    @Getter
    private String mailPassword;

    @Value("${mail.from}")
    @Getter
    private String mailFrom;
}
