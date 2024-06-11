package com.example.cashcard.enricher;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.cashcard.domain.EnrichedTransaction;
import com.example.cashcard.domain.Transaction;

@Configuration
public class CashCardTransactionEnricher {

    @Bean
    public Function<Transaction, EnrichedTransaction> enrichTransaction(EnricherService enricherService) {
        return transaction -> {
            return enricherService.enrichTransaction(transaction);
        };

    }

    @Bean
    EnricherService enricherService() {
        return new EnricherService();
    }
}
