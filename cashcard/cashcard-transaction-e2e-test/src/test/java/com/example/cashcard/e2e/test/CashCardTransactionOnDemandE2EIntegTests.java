package com.example.cashcard.e2e.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;

import com.example.cashcard.controller.CashCardController;
import com.example.cashcard.domain.CashCard;
import com.example.cashcard.domain.Transaction;
import com.example.cashcard.enricher.CashCardTransactionEnricher;
import com.example.cashcard.ondemand.CashCardTransactionOnDemand;
import com.example.cashcard.sink.CashCardTransactionSink;

@SpringBootTest(classes = CashCardTransactionOnDemandE2EIntegTests.OnDemandTestConfig.class, properties = {
        "spring.cloud.function.definition=enrichTransaction;cashCardTransactionFileSink",
        "spring.cloud.stream.bindings.approvalRequest-out-0.destination=approval-requests",
        "spring.cloud.stream.bindings.enrichTransaction-in-0.destination=approval-requests",
        "spring.cloud.stream.bindings.enrichTransaction-out-0.destination=enriched-transactions",
        "spring.cloud.stream.bindings.cashCardTransactionFileSink-in-0.destination=enriched-transactions"
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka
public class CashCardTransactionOnDemandE2EIntegTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void cashCardTransactionOnDemandEndToEnd() throws IOException {

        Path path = Paths.get("/tmp/transactions-output.csv");
        if (Files.exists(path)) {

            Files.delete(path);
        }
        Transaction transaction = new Transaction(1L, new CashCard(123L, "Foo Bar", 1.00));
        this.restTemplate.postForEntity("http://localhost:" + port + "/publish/txn", transaction, Transaction.class);

        Awaitility.await().until(() -> Files.exists(path));

        List<String> lines = Files.readAllLines(path);

        assertThat(lines.get(0)).contains("Foo Bar");
    }


    @EnableAutoConfiguration
    @Import({CashCardController.class, CashCardTransactionOnDemand.class, CashCardTransactionEnricher.class, CashCardTransactionSink.class})
    public static class OnDemandTestConfig {

    }
}
