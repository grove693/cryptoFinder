package com.moneymaker.cryptoFinder.services.job;

import com.moneymaker.cryptoFinder.clients.BinanceClient;
import com.moneymaker.cryptoFinder.model.CryptoPairPrice;
import com.moneymaker.cryptoFinder.moneyMaker.CryptoRepo;
import com.moneymaker.cryptoFinder.services.mail.MailNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PriceTracker {

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceTracker.class);

    @Autowired
    private BinanceClient binanceClient;

    @Autowired
    private MailNotificationService mailNotification;

    @Autowired
    private CryptoRepo cryptoRepo;

    @Scheduled(fixedRateString = "${binance.poll.interval.ms:1800000}")
    public void getCurrentPrices() {

        List<CryptoPairPrice> prices = binanceClient.getCurrentPrices();
        LOGGER.info("Retrieved {} crypto pairs from Binance", prices.size());
        Map<CryptoPairPrice, Double> winnerCryptos = cryptoRepo.detectPriceIncrease(prices);
        if (!winnerCryptos.isEmpty()){
            mailNotification.sendAlert(winnerCryptos);
        }
    }
}
