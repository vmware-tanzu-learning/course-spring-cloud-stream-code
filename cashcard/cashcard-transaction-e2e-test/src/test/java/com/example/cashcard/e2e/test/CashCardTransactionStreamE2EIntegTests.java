package com.example.cashcard.e2e.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;

import com.example.cashcard.enricher.CashCardTransactionEnricher;
import com.example.cashcard.sink.CashCardTransactionSink;
import com.example.cashcard.stream.CashCardTransactionStream;

@SpringBootTest(classes = CashCardTransactionStreamE2EIntegTests.StreamTestConfig.class, properties = {
        "spring.cloud.function.definition=approvalRequest;enrichTransaction;cashCardTransactionFileSink",
        "spring.cloud.stream.bindings.approvalRequest-out-0.destination=approval-requests",
        "spring.cloud.stream.bindings.enrichTransaction-in-0.destination=approval-requests",
        "spring.cloud.stream.bindings.enrichTransaction-out-0.destination=enriched-transactions",
        "spring.cloud.stream.bindings.cashCardTransactionFileSink-in-0.destination=enriched-transactions"
})
@EmbeddedKafka
public class CashCardTransactionStreamE2EIntegTests {

    @Test
    void cashCardTransactionStreamEndToEnd() throws IOException {
        Path path = Paths.get("/tmp/transactions-output.csv");
        if (Files.exists(path)) {
            Files.delete(path);
        }
        Awaitility.await().until(() -> Files.exists(path));
        List<String> lines = Files.readAllLines(path);
        assertThat(lines.get(0)).contains("John Doe");
    }

    @EnableAutoConfiguration
    @Import({CashCardTransactionStream.class, CashCardTransactionEnricher.class, CashCardTransactionSink.class})
    public static class StreamTestConfig {

    }

}
