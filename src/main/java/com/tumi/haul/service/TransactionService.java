package com.tumi.haul.service;

import com.tumi.haul.model.transactions.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    public Optional<Transaction> createTransaction(Transaction transaction);
    public void deleteTransaction(Long idChar);
    public List<Transaction> getAllTransactions();
    public Optional<Transaction>getTransactionById(Long idChar);

}
