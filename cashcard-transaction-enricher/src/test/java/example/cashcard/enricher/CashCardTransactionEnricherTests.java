package example.cashcard.enricher;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.cashcard.domain.*;
import example.cashcard.service.EnrichmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
public class CashCardTransactionEnricherTests {

  @MockBean
  private EnrichmentService enrichmentService;

  @Test
  void enrichmentServiceShouldAddDataToTransactions(@Autowired InputDestination inputDestination,
                      @Autowired OutputDestination outputDestination) throws IOException {

    Transaction transaction = new Transaction(1L, new CashCard(123L, "Kumar Patel", 1.00));
    EnrichedTransaction enrichedTransaction = new EnrichedTransaction(
      transaction.id(),
      transaction.cashCard(),
      ApprovalStatus.APPROVED,
      new CardHolderData(UUID.randomUUID(), transaction.cashCard().owner(), "123 Main Street"));

    given(enrichmentService.enrichTransaction(transaction)).willReturn(enrichedTransaction);

    Message<Transaction> message = MessageBuilder.withPayload(transaction).build();
    inputDestination.send(message, "enrichTransaction-in-0");
    Message<byte[]> result = outputDestination.receive(5000, "enrichTransaction-out-0");
    assertThat(result).isNotNull();

    ObjectMapper objectMapper = new ObjectMapper();
    EnrichedTransaction receivedData = objectMapper.readValue(result.getPayload(), EnrichedTransaction.class);
    assertThat(receivedData).isEqualTo(enrichedTransaction);
  }

  @SpringBootApplication
  public static class App {

  }
}