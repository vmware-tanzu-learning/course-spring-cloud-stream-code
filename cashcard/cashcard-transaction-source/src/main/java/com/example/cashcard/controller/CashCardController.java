package com.example.cashcard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.cashcard.domain.Transaction;
import com.example.cashcard.ondemand.CashCardTransactionOnDemand;

@RestController
public class CashCardController {

    private final CashCardTransactionOnDemand cashCardTransactionOnDemand;

    public CashCardController(@Autowired CashCardTransactionOnDemand cashCardTransactionOnDemand) {
        this.cashCardTransactionOnDemand = cashCardTransactionOnDemand;
    }

    @PostMapping(path = "/publish/txn")
    public void publishTxn(@RequestBody Transaction transaction) {
        this.cashCardTransactionOnDemand.publishOnDemand(transaction);
    }
}
