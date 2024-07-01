package example.cashcard.e2e.test;

import example.cashcard.controller.CashCardController;
import example.cashcard.domain.CashCard;
import example.cashcard.domain.Transaction;
import example.cashcard.enricher.CashCardTransactionEnricher;
import example.cashcard.ondemand.CashCardTransactionOnDemand;
import example.cashcard.sink.CashCardTransactionSink;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// Enable Embedded Kafka
@EmbeddedKafka
@SpringBootTest(classes = CashCardTransactionOnDemandE2ETests.OnDemandTestConfig.class, properties = {
  "spring.cloud.function.definition=enrichTransaction;cashCardTransactionFileSink",
  "spring.cloud.stream.bindings.approvalRequest-out-0.destination=approval-requests",
  "spring.cloud.stream.bindings.enrichTransaction-in-0.destination=approval-requests",
  "spring.cloud.stream.bindings.enrichTransaction-out-0.destination=enriched-transactions",
  "spring.cloud.stream.bindings.cashCardTransactionFileSink-in-0.destination=enriched-transactions"
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CashCardTransactionOnDemandE2ETests {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  void cashCardTransactionOnDemandEndToEnd() throws IOException {
    // Remove the old sink-file if needed.
    Path path = Paths.get(CashCardTransactionSink.CSV_FILE_PATH);
    if (Files.exists(path)) {
      Files.delete(path);
    }

    // Create a transaction to POST
    Transaction transaction = new Transaction(1122334455L, new CashCard(6677889900L, "kumar2", 820.0));

    // POST the transaction to our SpringBridge REST API
    this.restTemplate.postForEntity("http://localhost:" + port + "/publish/txn", transaction, Transaction.class);

    // Wait for the sink file to appear and fetch the first line
    Awaitility.await().until(() -> Files.exists(path));
    List<String> lines = Files.readAllLines(path);
    String csvLine = lines.get(0);

    // Test for information we know about the enriched transactions
    assertThat(csvLine).contains("1122334455");
    assertThat(csvLine).contains("6677889900");
    assertThat(csvLine).contains("kumar2");
    assertThat(csvLine).contains("820.0");
  }


  // Configure auto-configuration of embedded Kafka for the individual application configurations
  // Also, load the Source controller so we can send HTTP requests to it
  @EnableAutoConfiguration
  @Import({CashCardController.class, CashCardTransactionOnDemand.class, CashCardTransactionEnricher.class, CashCardTransactionSink.class})
  public static class OnDemandTestConfig {

  }
}
