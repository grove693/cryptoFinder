package com.moneymaker.cryptoFinder.moneyMaker;

import com.moneymaker.cryptoFinder.model.CryptoPairPrice;
import com.moneymaker.cryptoFinder.moneyMaker.utils.NumberUtilitary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class CryptoRepo {

    private Map<String, CryptoPairPrice> lastPrices = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(CryptoRepo.class);

    @Value("${cryptoFinder.percent.price.increase}")
    private Double percentIncreaseThreshold;

    private static final String BTC_SYMBOL = "BTC";
    private static final String USDT_SYMBOL = "USDT";

    public Map<CryptoPairPrice, Double> detectPriceIncrease(List<CryptoPairPrice> currentPrices) {
        Map<CryptoPairPrice, Double> winnerCryptos = currentPrices.stream()
                .filter(this::isDollarOrBtcPair)
                .filter(this::hasPriceIncreaseOverThreshold)
                .collect(Collectors.toMap(
                        cryptoPairPrice -> cryptoPairPrice,
                        cryptoPair -> NumberUtilitary.computePercentageDifference(
                                lastPrices.get(cryptoPair.getSymbol()).getPrice(),
                                cryptoPair.getPrice()
                        )
                ));

        //update map
        lastPrices = currentPrices.stream().collect(Collectors.toMap(
                CryptoPairPrice::getSymbol,
                cryptoPairPrice -> cryptoPairPrice
        ));
        LOGGER.info("Found {} cryptos with price increase", winnerCryptos.size());
        return winnerCryptos;
    }

    private boolean isDollarOrBtcPair(CryptoPairPrice cryptoPair) {
        if (cryptoPair.getSymbol().contains(BTC_SYMBOL)
                || cryptoPair.getSymbol().contains(USDT_SYMBOL)) {
            return true;
        }
        return false;
    }

    private boolean hasPriceIncreaseOverThreshold(CryptoPairPrice cryptoPair) {
        String pairSymbol = cryptoPair.getSymbol();

        if (!lastPrices.containsKey(pairSymbol)) {
            return false;
        }

        double percentageDifference = NumberUtilitary.computePercentageDifference(
                lastPrices.get(pairSymbol).getPrice(),
                cryptoPair.getPrice()
        );

        if (percentageDifference >= percentIncreaseThreshold) {
            return true;
        }

        return false;
    }


}
