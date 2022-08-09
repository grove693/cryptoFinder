package com.moneymaker.cryptoFinder.services.mail;

import com.moneymaker.cryptoFinder.model.CryptoPairPrice;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MailNotificationService {

    @Value("#{'${cryptoFinder.mail.notifications.emails}'.split(',')}")
    private List<String> destinationEmails;

    @Autowired
    private Mailer mailer;

    @Autowired
    private EmailCreator emailCreator;

    public void sendAlert( Map<CryptoPairPrice, Double> winnerCryptos){
        mailer.sendMail(emailCreator.buildEmail(destinationEmails, winnerCryptos));
    }



}
