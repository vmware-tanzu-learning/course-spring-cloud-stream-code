package com.example.cashcard.stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;

import com.example.cashcard.domain.CashCard;
import com.example.cashcard.domain.Transaction;
import com.example.cashcard.service.DataSourceService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class CashCardTransactionStreamTests {

	@MockBean
	private DataSourceService dataSourceService;

	@Test
	void basicCashCardSupplier(@Autowired OutputDestination outputDestination) throws IOException {
		given(dataSourceService.getData()).willReturn(new Transaction(1L, new CashCard(123L, "Foo Bar", 1.00)));

		Message<byte[]> result = outputDestination.receive(5000, "approvalRequest-out-0");
		assertThat(result).isNotNull();
		ObjectMapper objectMapper = new ObjectMapper();
		Transaction transaction = objectMapper.readValue(result.getPayload(), Transaction.class);
		assertThat(transaction.id()).isEqualTo(1L);
	}

	@SpringBootApplication
	public static class App {

	}


}
