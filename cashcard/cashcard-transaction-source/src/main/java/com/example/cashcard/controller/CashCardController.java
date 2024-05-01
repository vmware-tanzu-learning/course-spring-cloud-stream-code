package com.example.cashcard.controller;

import com.example.cashcard.domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CashCardController {

    private final StreamBridge streamBridge;

    public CashCardController(@Autowired StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @PostMapping(path = "/publish/txn")
    public void publishTxn(@RequestBody Transaction transaction) {
        this.streamBridge.send("approvalRequest-out-0", transaction);
    }
}
