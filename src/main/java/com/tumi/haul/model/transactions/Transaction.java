package com.tumi.haul.model.transactions;


import com.tumi.haul.model.enums.PaymentMethod;
import com.tumi.haul.model.enums.TransactionStatus;
import com.tumi.haul.model.job.Job;
import com.tumi.haul.model.user.Client;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.io.Serializable;
@Data
public class Transaction implements Serializable {
    @Id
    private Long id;
    @ManyToOne
    private Job job;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Client hauler;
    private Double amount;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.PENDING;


}
