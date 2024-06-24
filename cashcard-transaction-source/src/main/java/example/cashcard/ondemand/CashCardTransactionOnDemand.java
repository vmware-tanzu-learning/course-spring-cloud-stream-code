package example.cashcard.ondemand;

import example.cashcard.domain.Transaction;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CashCardTransactionOnDemand {

  private final StreamBridge streamBridge;

  public CashCardTransactionOnDemand(StreamBridge streamBridge) {
    this.streamBridge = streamBridge;
  }

  public void publishOnDemand(Transaction transaction) {
    this.streamBridge.send("approvalRequest-out-0", transaction);
  }
}