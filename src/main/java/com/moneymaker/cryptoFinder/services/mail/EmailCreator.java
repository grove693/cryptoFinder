package com.moneymaker.cryptoFinder.services.mail;

import com.moneymaker.cryptoFinder.model.CryptoPairPrice;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class EmailCreator {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    private static final String EMAIL_SUBJECT_TEMPLATE = "Crypto Notification Alert: %s";

    private static final NumberFormat formatter = new DecimalFormat("###.###########");
    private static final String EMAIL_BODY_TEMPLATE = "Ticker: %s. Price increase: %s%%. Current Price: %s ";
    private static final String EMAIL_BODY_NO_PRICE_INCREASE = "No cryptos with price increase found";

    @Value("${cryptoFinder.mail.username}")
    private String username;

    public Email buildEmail(List<String> dest, Map<CryptoPairPrice, Double> winnerCryptos) {
        String subject = String.format(
                EMAIL_SUBJECT_TEMPLATE,
                getCurrentLocalDateTimeStamp()
        );
        return EmailBuilder.startingBlank()
                .toMultiple(dest)
                .withSubject(subject)
                .from(username)
                .withPlainText(buildBody(winnerCryptos))
                .buildEmail();
    }

    private String buildBody(Map<CryptoPairPrice, Double> winnerCryptos) {
        if (CollectionUtils.isEmpty(winnerCryptos)) {
            return EMAIL_BODY_NO_PRICE_INCREASE;
        }
        StringBuffer strBuffer = new StringBuffer("");
        for (Map.Entry<CryptoPairPrice, Double> entry : winnerCryptos.entrySet()) {
            String line = String.format(
                    EMAIL_BODY_TEMPLATE,
                    entry.getKey().getSymbol(),
                    formatter.format(entry.getValue()),
                    formatter.format(entry.getKey().getPrice())
            );
            strBuffer.append(line);
            strBuffer.append(System.lineSeparator());
        }
        return strBuffer.toString();
    }

    private String getCurrentLocalDateTimeStamp() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
    }
}
