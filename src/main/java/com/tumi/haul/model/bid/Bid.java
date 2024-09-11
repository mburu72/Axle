package com.tumi.haul.model.bid;


import com.tumi.haul.model.enums.BidStatus;
import com.tumi.haul.model.job.Job;
import com.tumi.haul.model.user.Client;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
public class Bid implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Job job;
    @ManyToOne
    private Client hauler;
    private String bidAmount;
   @Enumerated(EnumType.STRING)
    private final BidStatus status= BidStatus.PENDING;
    private final Date bidDate = new Date();


    public Bid() {}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bid bid = (Bid) o;
        return id.equals(bid.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }


}
