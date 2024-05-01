package com.example.cashcard.service;

import com.example.cashcard.domain.CashCard;
import com.example.cashcard.domain.Transaction;
import java.util.Random;

public class DataSourceService {

    public Transaction getData() {
        CashCard cashCard = new CashCard(new Random().nextLong(), "John Doe", 100.00);
        return new Transaction(new Random().nextLong(), cashCard);
    }
}
