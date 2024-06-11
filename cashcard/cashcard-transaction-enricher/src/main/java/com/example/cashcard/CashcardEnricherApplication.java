package com.example.cashcard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootApplication
public class CashcardEnricherApplication {

	public static void main(String[] args) throws JsonProcessingException {
		SpringApplication.run(CashcardEnricherApplication.class, args);
	}

}
