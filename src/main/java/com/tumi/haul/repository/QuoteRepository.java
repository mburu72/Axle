package com.tumi.haul.repository;

import com.tumi.haul.model.bid.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    List<Quote> findByJobId(Long jobId);
    List<Quote> findByHaulerId(Long haulerId);
}
