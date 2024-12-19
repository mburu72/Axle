package com.tumi.haul.service;

import com.tumi.haul.model.primitives.Amount;
import com.tumi.haul.model.primitives.PhoneNumber;
import com.tumi.haul.model.transactions.Transaction;
import com.tumi.haul.model.transactions.TransactionRequest;
import com.tumi.haul.repository.TransactionRepository;
import com.tumi.haul.service.safcom.IntaSend;
import net.sf.jasperreports.engine.JasperReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
@Service
public class TransactionServiceImpl implements TransactionService{
    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);
    @Autowired
    private TransactionRepository repository;
    @Autowired
    private IntaSend intaSend;

    /**
     * @return
     */
    @Override
    public String createTransaction(TransactionRequest transactionReq) throws IOException {
        log.info("Initiating payment...");
        String response = intaSend.initiatePayment(transactionReq);
        log.info("Here: {}", response);
        return response;
    }

    /**
     * @param idChar
     */
    @Override
    public void deleteTransaction(Long idChar) {

    }

    /**
     * @return
     */
    @Override
    public List<Transaction> getAllTransactions() {
        return List.of();
    }

    /**
     * @param idChar
     * @return
     */
    @Override
    public Optional<Transaction> getTransactionById(Long idChar) {
        return Optional.empty();
    }
}
