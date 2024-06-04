package com.example.cashcard.stream;

import com.example.cashcard.service.DataSourceService;
import com.example.cashcard.domain.Transaction;
import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CashCardTransactionStream {

    @Bean
    public Supplier<Transaction> approvalRequest(DataSourceService dataSource) {
        return () -> {
            return dataSource.getData();
        };
    }

    @Bean
    public DataSourceService dataSourceFacade() {
            return new DataSourceService();
    }

}
