package com.example.cashcard.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.cashcard.domain.CashCard;
import com.example.cashcard.domain.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(TestChannelBinderConfiguration.class)
public class CashCardControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void basicCashCardSupplier1(@Autowired OutputDestination outputDestination) throws IOException {
        Transaction transaction = new Transaction(1L, new CashCard(123L, "Foo Bar", 1.00));
        this.restTemplate.postForEntity("http://localhost:" + port + "/publish/txn", transaction, Transaction.class);

        Message<byte[]> result = outputDestination.receive(5000, "approvalRequest-out-0");
        assertThat(result).isNotNull();
        ObjectMapper objectMapper = new ObjectMapper();
        Transaction transaction1 = objectMapper.readValue(result.getPayload(), Transaction.class);
        assertThat(transaction1.id()).isEqualTo(1L);
    }

    @SpringBootApplication
    public static class App {

    }

}
