package com.example.cashcard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.cashcard.domain.CashCard;
import com.example.cashcard.domain.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class CashcardApplication {

	public static void main(String[] args) throws JsonProcessingException {

		ObjectMapper objectMapper = new ObjectMapper();

		Transaction transaction = new Transaction(100L, new CashCard(209L, "foo", 200.00));
		System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(transaction));
		//SpringApplication.run(CashcardApplication.class, args);
	}




}
