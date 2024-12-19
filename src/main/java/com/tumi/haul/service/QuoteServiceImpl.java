package com.tumi.haul.service;

import com.tumi.haul.model.bid.Quote;
import com.tumi.haul.model.bid.QuoteResp;
import com.tumi.haul.model.enums.JobStatus;
import com.tumi.haul.model.enums.NotificationType;
import com.tumi.haul.model.enums.QuoteStatus;
import com.tumi.haul.model.job.Job;
import com.tumi.haul.model.notification.Notification;
import com.tumi.haul.model.user.User;
import com.tumi.haul.model.user.UserResp;
import com.tumi.haul.repository.JobRepository;
import com.tumi.haul.repository.QuoteRepository;
import com.tumi.haul.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class QuoteServiceImpl implements QuoteService {
    @Autowired
   private KafkaTemplate<String, Notification> kafkaTemplate;
    @Value("${image.storage.path}")
    private String imageStoragePath;
    private static final Logger log = LoggerFactory.getLogger(QuoteServiceImpl.class);
    private final QuoteRepository quoteRepository;
    private final JobRepository jobRepository;
    private final UserRepository haulerRepository;
    private final JobService jobService;

    public QuoteServiceImpl(QuoteRepository quoteRepository, JobRepository jobRepository, UserRepository haulerRepository, JobService jobService) {
        this.quoteRepository = quoteRepository;
        this.jobRepository = jobRepository;
        this.haulerRepository = haulerRepository;
        this.jobService = jobService;
    }

    @Override
    public String submitQuote(Quote quote, String jobId, String haulerId, MultipartFile image) throws IOException {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found!"));

        User hauler = haulerRepository.findById(haulerId)
                .orElseThrow(() -> new RuntimeException("Hauler not found"));
        String jobOwnerId =  job.getOwner().getId();
//       String message = hauler.getFirstName().getValue() + " " + hauler.getLastName().getValue() + " " + "quoted your job" + jobOwnerId;
//        Notification notification = new Notification(jobOwnerId, message);
//       kafkaTemplate.send("quote-alert", notification);
        String imageName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        Path imageUrl = Path.of(imageStoragePath).resolve(imageName);
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
        quoteRepository.save(quote);
        return "Submitted quote successfully!";



    }

    @Override
    public Quote acceptQuote(String quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found!"));
        String jobId = quote.getJob().getId();
        log.info("Quote Service Impl : {}", jobId);
        Job updatedJob = jobRepository.findById(jobId).orElseThrow(()-> new RuntimeException("job not found"));
        jobService.updateJobStatus(jobId, updatedJob);
        String quoteOwner = quote.getHauler().getId();
        if (quote.getStatus() == QuoteStatus.PENDING) {
            String message = "Your quote was accepted!";
            quote.setStatus(QuoteStatus.ACCEPTED);
            updatedJob.setStatus(JobStatus.QUOTED);
            List<Quote> otherQuoteList = quoteRepository.findByJobId(jobId);
            for (Quote otherQuote:otherQuoteList){
                if (otherQuote.getStatus() ==  QuoteStatus.PENDING){
                    rejectQuote(otherQuote.getId());
                }
            }
//            Notification notification =new Notification (quoteOwner, message);
//            kafkaTemplate.send("quote-status", notification);
            return quoteRepository.save(quote);
        }
        throw new RuntimeException("Quote already processed!");
    }

    @Override
    public Quote rejectQuote(String quoteId) {

        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found!"));
        String quoteOwner = quote.getHauler().getId();
        if (quote.getStatus() == QuoteStatus.PENDING) {
            //String message = "Your quote was rejected!";
            quote.setStatus(QuoteStatus.REJECTED);
//            Notification notification =new Notification (quoteOwner, message);
//            kafkaTemplate.send("quote-status", notification);
            return quoteRepository.save(quote);
        }
        throw new RuntimeException("Quote already processed!");
    }


    @Override
    public void deleteQuote(String quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found!"));
        quoteRepository.delete(quote);
    }

    @Override
    public List<QuoteResp> getQuoteByHaulerIdAndJobId(String haulerId, String jobId) {
      List<Quote> quotes = quoteRepository.findByHaulerIdAndJobId(haulerId, jobId);
      List<QuoteResp> quoteRespList = new ArrayList<>();
        for (Quote quote : quotes){
            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/images/")
                    .path(quote.getImageName()) // Ensure this is the image name
                    .toUriString();
            QuoteResp quoteResp= new QuoteResp(
                    quote.getId(),
                    null,
                    quote.getStatus().toString(),
                    quote.getQuoteAmount().toString(),
                    quote.getExecutionDate(),
                    imageUrl
            );
            quoteRespList.add(quoteResp);

        }
        return quoteRespList;
    }

    @Override
    public QuoteResp getQuoteById(String quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found!"));
        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/images/")
                .path(quote.getImageName()) // Ensure this is the image name
                .toUriString();
        User owner = quote.getHauler();
        UserResp haulerResp = new UserResp(
                owner.getId(),
                owner.getFirstName().getValue(),
                owner.getLastName().getValue(),
                owner.getPhoneNumber().getValue(),
                owner.getRole().getAuthority()
        );

        return new QuoteResp(
                quote.getId(),
                haulerResp.firstName() + " " + haulerResp.lastName(),
                quote.getStatus().toString(),
                quote.getQuoteAmount().toString(),
                quote.getExecutionDate(),
                imageUrl
        );
    }

    @Override
    public List<QuoteResp> getQuoteByJobId(String jobId) {
        List<Quote> quoteList = quoteRepository.findByJobId(jobId);
        QuoteResp quoteResp;
        List<QuoteResp> quotes = new ArrayList<>();
        for (Quote quote : quoteList) {
            User owner = quote.getHauler();
            UserResp haulerResp = new UserResp(
                    owner.getId(),
                    owner.getFirstName().getValue(),
                    owner.getLastName().getValue(),
                    owner.getPhoneNumber().getValue(),
                    owner.getRole().getAuthority()
            );
            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/images/")
                    .path(quote.getImageName()) // Ensure this is the image name
                    .toUriString();
            quoteResp = new QuoteResp(
                    quote.getId(),
                    haulerResp.firstName() + " " + haulerResp.lastName(),
                    quote.getStatus().toString(),
                    quote.getQuoteAmount().toString(),
                    quote.getExecutionDate(),
                    imageUrl

            );
            quotes.add(quoteResp);
        }
        return quotes;

    }
}
