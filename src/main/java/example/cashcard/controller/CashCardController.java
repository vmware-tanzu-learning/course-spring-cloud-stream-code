package example.cashcard.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import example.cashcard.domain.Transaction;
import example.cashcard.ondemand.CashCardTransactionOnDemand;

@RestController
public class CashCardController {

  private CashCardTransactionOnDemand cashCardTransactionOnDemand;

  public CashCardController(CashCardTransactionOnDemand cashCardTransactionOnDemand) {
    this.cashCardTransactionOnDemand = cashCardTransactionOnDemand;
  }

  @PostMapping(path = "/publish/txn")
  public void publishTxn(@RequestBody Transaction transaction) {
    this.cashCardTransactionOnDemand.publishOnDemand(transaction);
  }
}