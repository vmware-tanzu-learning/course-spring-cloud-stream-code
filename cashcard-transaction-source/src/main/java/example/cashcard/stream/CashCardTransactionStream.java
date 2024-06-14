package example.cashcard.stream;

import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import example.cashcard.domain.Transaction;
import example.cashcard.service.DataSourceService;

@Configuration
public class CashCardTransactionStream {

    @Bean
    public Supplier<Transaction> approvalRequest(DataSourceService dataSource) {
        return () -> {
            return dataSource.getData();
        };
    }

    @Bean
    public DataSourceService dataSourceService() {
        return new DataSourceService();
    }
}
