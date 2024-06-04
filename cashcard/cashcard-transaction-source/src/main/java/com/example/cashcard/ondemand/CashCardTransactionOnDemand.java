package com.example.cashcard.ondemand;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Configuration;

import com.example.cashcard.domain.Transaction;

@Configuration
public class CashCardTransactionOnDemand {

    private final StreamBridge streamBridge;

    public CashCardTransactionOnDemand(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    /**
     * When calling this method, the provided {@link Transaction} will be sent to a middleware destination.
     *
     * @param transaction {@link Transaction} to sent to the destination
     */
    public void publishOnDemand(Transaction transaction) {
        this.streamBridge.send("approvalRequest-out-0", transaction);
    }
}
