package com.tumi.haul.service;

import com.tumi.haul.model.bid.Bid;
import com.tumi.haul.model.job.Job;

import java.util.List;
import java.util.Optional;

public interface BidService {
    public Optional<Bid> createBid(Bid bid);
    public void deleteBid(Bid bid);
    public List<Bid> getAllBids();
    public Optional<Bid>getBidById(Long id);
    public Optional<Bid>getBidByJob(Job job);
}
