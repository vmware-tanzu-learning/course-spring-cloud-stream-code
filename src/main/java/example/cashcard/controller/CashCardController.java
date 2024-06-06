package example.cashcard.controller;

import example.cashcard.domain.Transaction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CashCardController {

  @PostMapping(path = "/publish/txn")
  public void publishTxn(@RequestBody Transaction transaction) {
    System.out.println("POST for Transaction: " + transaction);
  }
}