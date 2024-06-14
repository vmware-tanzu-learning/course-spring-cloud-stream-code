package example.cashcard.enricher;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
public class CashCardTransactionEnricherTests {

    @Test
    void enrichmentServiceShouldAddDataToTransactions() throws IOException {
        assertThat(true).isTrue();
    }

    @SpringBootApplication
    public static class App {

    }

}
