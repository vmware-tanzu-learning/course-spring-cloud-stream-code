package example.cashcard.enricher;

import java.util.function.Function;

import example.cashcard.domain.EnrichedTransaction;
import example.cashcard.domain.Transaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import example.cashcard.service.EnrichmentService;

@Configuration
public class CashCardTransactionEnricher {

  @Bean
  EnrichmentService enricherService() {
    return new EnrichmentService();
  }

  @Bean
  public Function<Transaction, EnrichedTransaction> enrichTransaction(EnrichmentService enrichmentService) {
    return transaction -> {
      return enrichmentService.enrichTransaction(transaction);
    };
  }
}
