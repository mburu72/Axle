package com.tumi.haul.controller;

import com.tumi.haul.model.transactions.TransactionRequest;
import com.tumi.haul.service.TransactionService;
import com.tumi.haul.service.safcom.IntaSend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/payment")
public class IntasendController {
    private static final Logger log = LoggerFactory.getLogger(IntasendController.class);
    @Autowired
    private IntaSend intaSend;
    @Autowired
    private TransactionService transactionService;
    @PostMapping("/pay-now")
    public ResponseEntity<?> initiatePayment(@RequestBody TransactionRequest transactionRequest) throws IOException {
   try{
       transactionService.createTransaction(transactionRequest);
       return ResponseEntity.status(HttpStatus.OK).body("Payment completed successfully");
   }catch (Exception e){
       log.info(e.getLocalizedMessage());
       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getLocalizedMessage());
   }
    }
}
