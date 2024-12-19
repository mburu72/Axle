package com.tumi.haul.service;

import com.tumi.haul.model.bid.Quote;
import com.tumi.haul.model.bid.QuoteResp;
import com.tumi.haul.model.primitives.UserId;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface QuoteService {
    public String submitQuote(Quote quote, String jobId, String haulerId, MultipartFile image) throws IOException;
    public Quote acceptQuote(String quoteId);
    public Quote rejectQuote(String quoteId);
    public void deleteQuote(String quoteId);
    public List<QuoteResp> getQuoteByHaulerIdAndJobId(String id, String jobId);
    public QuoteResp getQuoteById(String quoteId);
    public List<QuoteResp>getQuoteByJobId(String jobId);
}