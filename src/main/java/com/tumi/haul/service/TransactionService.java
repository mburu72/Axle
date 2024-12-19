package com.tumi.haul.service;

import com.tumi.haul.model.transactions.Transaction;
import com.tumi.haul.model.transactions.TransactionRequest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TransactionService {
    public String createTransaction(TransactionRequest transactionRequest) throws IOException;
    public void deleteTransaction(Long idChar);
    public List<Transaction> getAllTransactions();
    public Optional<Transaction>getTransactionById(Long idChar);

}
