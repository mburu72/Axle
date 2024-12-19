package com.tumi.haul.repository;

import com.tumi.haul.model.bid.Quote;
import com.tumi.haul.model.primitives.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, String> {
    List<Quote> findByJobId(String jobId);
    List<Quote> findByHaulerIdAndJobId(String haulerId, String jobId);
}
