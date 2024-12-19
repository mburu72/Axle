package com.tumi.haul.model.transactions;

import lombok.Getter;

@Getter
public class TransactionRequest {
    public String amount;
    public String phoneNumber;
    public String jobId;
}
