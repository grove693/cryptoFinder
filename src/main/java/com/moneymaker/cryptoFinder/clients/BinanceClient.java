package com.moneymaker.cryptoFinder.clients;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneymaker.cryptoFinder.model.CryptoPairPrice;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class BinanceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(BinanceClient.class);

    @Value("${binance.endpoint:https://api.binance.com}")
    private String binanceEndpoint;

    @Value("${binance.api.path}")
    private String apiPath;

    @Value("${binance.appKey}")
    private String appKey;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(1000, TimeUnit.MILLISECONDS)
            .writeTimeout(1000, TimeUnit.MILLISECONDS)
            .build();

    private Request createRequest() {
        String url = String.format("%s%s", binanceEndpoint, apiPath);
        return new Request.Builder()
                .url(url)
                .addHeader("X-MBX-APIKEY", appKey)
                .get()
                .build();
    }

    private List<CryptoPairPrice> getPricesFromResponse(Response response) {
        try {
            return createModel(response.body().string());
        } catch (IOException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    private List<CryptoPairPrice> createModel(String json) {
        try {
            return MAPPER.readValue(json, new TypeReference<List<CryptoPairPrice>>() {
            });
        } catch (IOException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    public List<CryptoPairPrice> getCurrentPrices() {

        try {
            Request req = createRequest();
            LOGGER.info("Sending request to Binance Exchange...");
            Response response = client.newCall(req).execute();
            LOGGER.info("Response received with status code {}", response.code());
            if (response.isSuccessful()) {
                return getPricesFromResponse(response);
            }
            return Collections.EMPTY_LIST;
        } catch (IOException ex) {
            LOGGER.error("Error during respose processing from Binance", ex);
            return Collections.EMPTY_LIST;
        }
    }

}
