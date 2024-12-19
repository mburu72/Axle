package com.tumi.haul.model.transactions;


import com.tumi.haul.model.converters.AmountConverter;
import com.tumi.haul.model.converters.PhoneNumberConverter;
import com.tumi.haul.model.primitives.Amount;
import com.tumi.haul.model.primitives.PhoneNumber;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
public class Transaction implements Serializable {
    @Id
    private String id;
    @PrePersist
    public void generateId(){
        if (this.id == null){
            this.id = "t_" + UUID.randomUUID().toString().substring(0, 8).toLowerCase();
        }
    }
    private String jobId;
    @Convert(converter = AmountConverter.class)
    private Amount amount;
    @Convert(converter = PhoneNumberConverter.class)
    private PhoneNumber phoneNumber;



}
