package com.tumi.haul.controller;


import com.tumi.haul.model.bid.Quote;
import com.tumi.haul.model.bid.QuoteResp;
import com.tumi.haul.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {
    private static final Logger log = LoggerFactory.getLogger(QuoteController.class);
    @Autowired
    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping("/{jobId}/{haulerId}/submit")
    public ResponseEntity<?> submitQuote(@PathVariable Long jobId, @PathVariable Long haulerId , @RequestPart("image") MultipartFile image, @RequestPart("quote") Quote quote){
      try{
          quoteService.submitQuote(quote, jobId,haulerId, image);
          return ResponseEntity.status(HttpStatus.OK).body("Submitted quote successfully!");
      }catch (Exception e){
          log.info("well this happened: {}",e.getLocalizedMessage());
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
      }

    }
    @PutMapping("/{quoteId}/accept")
    public ResponseEntity<?> acceptQuote(@PathVariable Long quoteId){
        try{
            quoteService.acceptQuote(quoteId);
            return ResponseEntity.status(HttpStatus.OK).body("Quote accepted!");
        }catch (Exception e){
            log.warn("HERE!:{}",e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
        }
    }
    @PutMapping("/{quoteId}/reject")
    public ResponseEntity<?> rejectQuote(@PathVariable Long quoteId){
        try{
            quoteService.rejectQuote(quoteId);
            QuoteResp quoteResp = quoteService.getQuoteById(quoteId);
           String name = quoteResp.ownerDetails();
            return ResponseEntity.status(HttpStatus.OK).body("Quote posted by" + name + "rejected!" );
        }catch (Exception e){
            log.warn("HERE!:{}",e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
        }
    }

    @DeleteMapping("/{quoteId}/delete")
    public ResponseEntity<?>deleteQuote(@PathVariable Long quoteId){
        try{
            quoteService.deleteQuote(quoteId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted quote");
        }catch (Exception e){
            log.warn("HERE!:{}",e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to delete quote");
        }

    }

    @GetMapping("/{quoteId}")
    public ResponseEntity<?>getQuoteById(@PathVariable Long quoteId){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(quoteService.getQuoteById(quoteId));
        }catch (Exception e){
            log.warn("HERE!:{}",e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.FOUND).body("Failed");
        }

    }
    @GetMapping("/{jobId}/quote")
    public ResponseEntity<?> getQuoteByJob(@PathVariable Long jobId){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(quoteService.getQuoteByJobId(jobId));
        }catch (Exception e){
            log.warn("HERE!:{}",e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getLocalizedMessage());
        }

    }
}
