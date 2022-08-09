package com.moneymaker.cryptoFinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptoFinderApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoFinderApplication.class, args);
	}

}
