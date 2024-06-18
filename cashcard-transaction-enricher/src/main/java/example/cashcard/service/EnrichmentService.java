package example.cashcard.service;

import java.util.UUID;

import example.cashcard.domain.ApprovalStatus;
import example.cashcard.domain.CardHolderData;
import example.cashcard.domain.EnrichedTransaction;
import example.cashcard.domain.Transaction;

public class EnrichmentService {
  public EnrichedTransaction enrichTransaction(Transaction transaction) {
    return new EnrichedTransaction(transaction.id(), transaction.cashCard(), ApprovalStatus.APPROVED,
      new CardHolderData(UUID.randomUUID(), transaction.cashCard().owner(), "123 Main street"));
  }
}