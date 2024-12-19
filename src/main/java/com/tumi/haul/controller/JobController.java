package com.tumi.haul.controller;


import com.tumi.haul.config.ValidImage;
import com.tumi.haul.model.job.Job;
import com.tumi.haul.model.user.Client;
import com.tumi.haul.repository.UserRepository;
import com.tumi.haul.service.JobService;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {
    private static final Logger log = LoggerFactory.getLogger(JobController.class);
    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService, UserRepository clientRepository) {
        this.jobService = jobService;
    }
    @PostMapping("/new")
    public ResponseEntity<?> addJobnClient(@RequestPart Client client, @RequestPart("job") Job job, @RequestPart("image") @ValidImage List<MultipartFile> image){
        try{
            jobService.addJobnClient(client, job, image);
            return  ResponseEntity.status(HttpStatus.CREATED).body("Created");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
    @PostMapping("/{clientId}/new")
    public ResponseEntity<?> addJob(@PathVariable String clientId, @RequestPart("job") Job job, @RequestPart("image") @ValidImage List<MultipartFile> image){
        log.info("This is the job created: {}", job.toString());
        try{
            jobService.createJob(clientId, job, image);
            return new  ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            log.error("FAILED!:{}", e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PutMapping("/update/{clientId}")
    public ResponseEntity<?>updateJob(@RequestParam String jobId, @PathVariable String clientId, @RequestBody Job updatedJob){
        try{
            jobService.updateJob(jobId,clientId, updatedJob);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Job updated successfully!");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getLocalizedMessage());
        }
    }
    @DeleteMapping("/delete/{clientId}")
    public ResponseEntity<?> deleteJob(@RequestParam String jobId, @PathVariable String clientId){
        try{
            jobService.deleteJob(jobId, clientId);
            return ResponseEntity.status(HttpStatus.OK).body("Deleted job successfully?");
        }catch (Exception e){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getLocalizedMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllJobs(){
        try{
            return  ResponseEntity.status(HttpStatus.OK).body(jobService.getAllJobs());
        }catch (Exception e){
            log.warn("HERE : {}", e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to load jobs");
        }

    }
    @GetMapping("/{clientId}/jobs")
    public ResponseEntity<?> getJobByClient(@PathVariable String clientId){
        try{
            ;
            return ResponseEntity.status(HttpStatus.OK).body(jobService.getJobByOwnerId(clientId));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable String id) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(jobService.getJobById(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getLocalizedMessage());
        }
    }

}
