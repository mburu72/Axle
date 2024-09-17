package com.tumi.haul.service;

import com.tumi.haul.model.bid.Quote;
import com.tumi.haul.model.bid.QuoteResp;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface QuoteService {
    public Quote submitQuote(Quote quote, Long jobId, Long haulerId, MultipartFile image) throws IOException;
    public Quote acceptQuote(Long id);
    public Quote rejectQuote(Long quoteId);
    public void deleteQuote(Long quoteId);
    public List<Quote> getQuoteByHaulerId(Long id);
    public QuoteResp getQuoteById(Long id);
    public List<QuoteResp>getQuoteByJobId(Long id);
}
