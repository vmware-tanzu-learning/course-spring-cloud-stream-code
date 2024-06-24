package example.cashcard.sink;

import example.cashcard.domain.*;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.cloud.function.definition=sinkToConsole;cashCardTransactionFileSink")
@Import(TestChannelBinderConfiguration.class)
@ExtendWith(OutputCaptureExtension.class)
public class CashCardTransactionSinkTests {
  private static final int AWAIT_DURATION = 10;

  @Test
  void sinkToConsole(@Autowired InputDestination inputDestination, CapturedOutput output) throws IOException {
    Transaction transaction = new Transaction(1L, new CashCard(123L, "John Doe", 1.00));
    EnrichedTransaction enrichedTransaction = new EnrichedTransaction(transaction.id(), transaction.cashCard(), ApprovalStatus.APPROVED,
      new CardHolderData(UUID.randomUUID(), transaction.cashCard().owner(), "123 Main Street"));

    Message<EnrichedTransaction> message = MessageBuilder.withPayload(enrichedTransaction).build();

    inputDestination.send(message, "sinkToConsole-in-0");

    Awaitility.await().atMost(Duration.ofSeconds(AWAIT_DURATION))
      .until(() -> output.toString().contains("Transaction Received: " + enrichedTransaction.toString()));
  }

  @Test
  void sinkToFile(@Autowired InputDestination inputDestination) throws IOException {
    Transaction transaction = new Transaction(1L, new CashCard(123L, "John Doe", 100.25));
    UUID uuid = UUID.fromString("65d0b699-3695-44c6-ba23-4a241717dab7");
    EnrichedTransaction enrichedTransaction = new EnrichedTransaction(transaction.id(), transaction.cashCard(), ApprovalStatus.APPROVED,
      new CardHolderData(uuid, transaction.cashCard().owner(), "123 Main Street"));

    Message<EnrichedTransaction> message = MessageBuilder.withPayload(enrichedTransaction).build();

    inputDestination.send(message, "cashCardTransactionFileSink-in-0");

    Path path = Paths.get(CashCardTransactionSink.CSV_FILE_PATH);
    Awaitility.await().until(() -> Files.exists(path));

    List<String> lines = Files.readAllLines(path);

    assertThat(lines.get(0)).isEqualTo
      ("1,123,100.25,John Doe,65d0b699-3695-44c6-ba23-4a241717dab7,123 Main Street,APPROVED");
  }


  @SpringBootApplication
  public static class App {

  }
}