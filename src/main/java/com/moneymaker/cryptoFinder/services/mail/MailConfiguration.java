package com.moneymaker.cryptoFinder.services.mail;

import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfiguration {

    @Value("${cryptoFinder.mail.username}")
    private String username;

    @Value("${cryptoFinder.mail.password}")
    private String password;

    @Value("${cryptoFinder.mail.smtpServer}")
    private String smtpServer;

    @Bean
    public Mailer getMailer(){
        return MailerBuilder
                .withSMTPServer(smtpServer,587,username,password)
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withDebugLogging(false)
                .async()
                .buildMailer();

    }
}
