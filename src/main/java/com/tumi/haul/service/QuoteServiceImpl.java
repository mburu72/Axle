package com.tumi.haul.service;

import com.tumi.haul.model.bid.Quote;
import com.tumi.haul.model.bid.QuoteResp;
import com.tumi.haul.model.enums.JobStatus;
import com.tumi.haul.model.enums.QuoteStatus;
import com.tumi.haul.model.job.Job;
import com.tumi.haul.model.job.JobResp;
import com.tumi.haul.model.user.Hauler;
import com.tumi.haul.model.user.HaulerResp;
import com.tumi.haul.repository.HaulerRepository;
import com.tumi.haul.repository.JobRepository;
import com.tumi.haul.repository.QuoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class QuoteServiceImpl implements QuoteService{
    private static final Logger log = LoggerFactory.getLogger(QuoteServiceImpl.class);
    private final QuoteRepository quoteRepository;
    private  final JobRepository jobRepository;
    private final HaulerRepository haulerRepository;

    public QuoteServiceImpl(QuoteRepository quoteRepository, JobRepository jobRepository, HaulerRepository haulerRepository) {
        this.quoteRepository = quoteRepository;
        this.jobRepository = jobRepository;
        this.haulerRepository = haulerRepository;
    }

    @Override
    public Quote submitQuote(Quote quote, Long jobId, Long haulerId, MultipartFile image) throws IOException {
        try{
            Job job = jobRepository.findById(jobId)
                    .orElseThrow(()-> new RuntimeException("Job not found!"));
            log.info("the job is {}", job.getPickupLocation().toString());
            Hauler hauler = haulerRepository.findById(haulerId)
                    .orElseThrow(()-> new RuntimeException("Hauler not found"));
            log.info("{} is creating the quote", hauler.getFirstName().toString());
            String imageName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
            Path imageUrl = Path.of("uploads/images").resolve(imageName);
            Path imageDir = imageUrl.getParent();
            if (!Files.exists(imageDir)) {
                Files.createDirectories(imageDir);
            }
            Files.copy(image.getInputStream(), imageUrl, StandardCopyOption.REPLACE_EXISTING);

            quote.setImageName(imageName);
            quote.setImageType(image.getContentType());
            quote.setImageUrl(imageUrl.toString());
            quote.setJob(job);
            quote.setHauler(hauler);


            return quoteRepository.save(quote);
        }catch (Exception e){
            log.warn(e.getLocalizedMessage());
            return null;
        }

    }

    @Override
    public Quote acceptQuote(Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(()-> new RuntimeException("Quote not found!"));
clone
if (quote.getStatus() == QuoteStatus.PENDING){
    quote.setStatus(QuoteStatus.ACCEPTED);
     return quoteRepository.save(quote);
}
throw new RuntimeException("Quote already processed!");
    }

    @Override
    public Quote rejectQuote(Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(()-> new RuntimeException("Quote not found!"));
        if (quote.getStatus() == QuoteStatus.PENDING){
            quote.setStatus(QuoteStatus.REJECTED);
            return quoteRepository.save(quote);
        }
        throw new RuntimeException("Quote already processed!");
    }


    @Override
    public void deleteQuote(Long quoteId) {
Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(()-> new RuntimeException("Quote not found!"));
quoteRepository.delete(quote);
    }

    @Override
    public List<Quote> getQuoteByHaulerId(Long haulerId) {
        return quoteRepository.findByHaulerId(haulerId);
    }

    @Override
    public QuoteResp getQuoteById(Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(()-> new RuntimeException("Quote not found!"));
        Hauler owner = quote.getHauler();
        HaulerResp haulerResp = new HaulerResp(
                owner.getFirstName().getValue(),
                owner.getLastName().getValue()
        );

        return new QuoteResp(
                quote.getId().toString(),
                haulerResp.firstName() + " " + haulerResp.lastName(),
                quote.getQuoteAmount(),
                quote.getExecutionDate(),
                quote.getStatus().toString()
                );
    }

    @Override
    public List<QuoteResp> getQuoteByJobId(Long jobId) {
        List<Quote> quoteList = quoteRepository.findByJobId(jobId);
        QuoteResp quoteResp ;
        List<QuoteResp> quotes = new ArrayList<>();
        for (Quote quote:quoteList){
            Hauler owner = quote.getHauler();
            HaulerResp haulerResp = new HaulerResp(
                    owner.getFirstName().getValue(),
                    owner.getLastName().getValue()
            );

            quoteResp = new QuoteResp(
                    quote.getId().toString(),
                    haulerResp.firstName() + " " + haulerResp.lastName(),
                    quote.getStatus().toString(),
                    quote.getQuoteAmount(),
                    quote.getExecutionDate()

            );
            quotes.add(quoteResp);
        }
        return  quotes;

    }
}
