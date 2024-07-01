package example.cashcard.sink;

import example.cashcard.domain.EnrichedTransaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.StringJoiner;
import java.util.function.Consumer;

@Configuration
public class CashCardTransactionSink {

  public static final String CSV_FILE_PATH = System.getProperty("user.dir") + "/build/tmp/transactions-output.csv";

  @Bean
  public Consumer<EnrichedTransaction> sinkToConsole() {
    return enrichedTransaction -> {
      System.out.println("Transaction Received: " + enrichedTransaction);
    };
  }

  @Bean
  public Consumer<EnrichedTransaction> cashCardTransactionFileSink() {
    return enrichedTransaction -> {
      StringJoiner joiner = new StringJoiner(",");
      StringJoiner enrichedTxnTextual = joiner.add(String.valueOf(enrichedTransaction.id()))
        .add(String.valueOf(enrichedTransaction.cashCard().id()))
        .add(String.valueOf(enrichedTransaction.cashCard().amountRequestedForAuth()))
        .add(enrichedTransaction.cardHolderData().name())
        .add(enrichedTransaction.cardHolderData().userId().toString())
        .add(enrichedTransaction.cardHolderData().address())
        .add(enrichedTransaction.approvalStatus().name());

      Path path = Paths.get(CSV_FILE_PATH);

      try {
        ensureOutputFileExists();
        Files.writeString(path, enrichedTxnTextual.toString() + "\n", StandardOpenOption.APPEND);
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    };
  }

  private void ensureOutputFileExists() {
    try {
      new File(CSV_FILE_PATH).createNewFile();
    } catch (IOException e) {
      // Ignore
    };
  }
}
