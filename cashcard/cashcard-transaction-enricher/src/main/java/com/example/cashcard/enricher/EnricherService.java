package com.example.cashcard.enricher;

import java.util.UUID;

import com.example.cashcard.domain.ApprovalStatus;
import com.example.cashcard.domain.CardHolderData;
import com.example.cashcard.domain.EnrichedTransaction;
import com.example.cashcard.domain.Transaction;

public class EnricherService {

    public EnrichedTransaction enrichTransaction(Transaction transaction) {
        return new EnrichedTransaction(transaction.id(), transaction.cashCard(), ApprovalStatus.APPPROVED,
                new CardHolderData(UUID.randomUUID(), transaction.cashCard().owner(), "123 Main street"));
    }
}
