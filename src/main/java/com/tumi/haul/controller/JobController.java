package com.tumi.haul.controller;


import com.tumi.haul.model.job.Job;
import com.tumi.haul.model.user.Client;
import com.tumi.haul.repository.ClientRepository;
import com.tumi.haul.service.JobService;
import jakarta.persistence.Access;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {
    private static final Logger log = LoggerFactory.getLogger(JobController.class);
    private final JobService jobService;
    private final ClientRepository clientRepository;
    @Autowired
    public JobController(JobService jobService, ClientRepository clientRepository) {
        this.jobService = jobService;
        this.clientRepository = clientRepository;
    }

    @PostMapping("/new")
    public ResponseEntity<?> createJob(@RequestParam Long clientId, @RequestBody Job job) {
        try{
            jobService.createJob(clientId, job);
            return ResponseEntity.status(org.springframework.http.HttpStatus.OK)
                    .body(HttpStatus.ACCEPTED);
        }catch (Exception e){
            log.warn(e.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getLocalizedMessage());

                    }

    }
    @PutMapping("/update/{clientId}")
    public ResponseEntity<?>updateJob(@RequestParam Long jobId, @PathVariable Long clientId, @RequestBody Job updatedJob){
        try{
            jobService.updateJob(jobId,clientId, updatedJob);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Job updated successfully!");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getLocalizedMessage());
        }
    }
    @PutMapping("/accept/{clientId}")
    public ResponseEntity<?>acceptJob(@RequestParam Long jobId, @PathVariable Long clientId){
        try{
            jobService.acceptJob(jobId, clientId);
            return ResponseEntity.status(org.springframework.http.HttpStatus.OK)
                    .body(HttpStatus.ACCEPTED);
        }catch (Exception e){
            log.warn(e.toString());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body("Job is not available!");


        }
    }
    @PutMapping("/complete/{clientId}")
    public ResponseEntity<?>completeJob(@RequestParam Long jobId, @PathVariable Long clientId){
        Job job = jobService.getJobById(jobId);
        log.info("job id is:{}", jobId.toString());
        log.info("client id is:{}", clientId.toString());
        try{
            jobService.completeJob(jobId, clientId);
            return ResponseEntity.status(org.springframework.http.HttpStatus.OK)
                    .body(HttpStatus.ACCEPTED);
        }catch (Exception e){
            log.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getLocalizedMessage());

        }
    }

    @DeleteMapping("/delete/{clientId}")
    public ResponseEntity<?> deleteJob(@RequestParam Long jobId, @PathVariable Long clientId){
        try{
            jobService.deleteJob(jobId, clientId);
          return ResponseEntity.status(HttpStatus.OK).body("Deleted job successfully?");
        }catch (Exception e){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getLocalizedMessage());
        }
    }
    @GetMapping
    public List<Job> getAllJobs(){
    return jobService.getAllJobs();
    }/*
    @GetMapping("/api/jobs/{id}")
    public Optional<Job>getJobById(@PathVariable IdChar id) {
        if (jobService.getJobById(id).isEmpty())
            throw new IllegalArgumentException("Job doesn't exist!");
        return jobService.getJobById(id);
    }*/

    }
