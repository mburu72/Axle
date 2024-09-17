package com.tumi.haul.controller;


import com.tumi.haul.model.job.Job;
import com.tumi.haul.repository.ClientRepository;
import com.tumi.haul.service.JobService;
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
    private final ClientRepository clientRepository;
    @Autowired
    public JobController(JobService jobService, ClientRepository clientRepository) {
        this.jobService = jobService;
        this.clientRepository = clientRepository;
    }
    @PostMapping("/{clientId}/new")
    public ResponseEntity<?> addJob(@PathVariable Long clientId, @RequestPart("job") Job job, @RequestPart("image") MultipartFile image){
        log.info("This is the job created: {}", job.toString());

     try{
         jobService.addJob(clientId, job, image);
         log.info("Job created!");

        return new  ResponseEntity<>(HttpStatus.CREATED);
     }catch (Exception e){
         log.error("FAILED!");
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
     }

    }
    /*
   public ResponseEntity<Resource> serveImage(@PathVariable Long id){

    }*/
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

    @PutMapping("/complete/{clientId}")
    public ResponseEntity<?>completeJob(@RequestParam Long jobId, @PathVariable Long clientId){
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
    }
    @GetMapping("/{clientId}/jobs")
    public ResponseEntity<?> getJobByClient(@PathVariable Long clientId){
        try{
           ;
            return ResponseEntity.status(HttpStatus.OK).body(jobService.getJobByOwnerId(clientId));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable Long id) {
try{
    return ResponseEntity.status(HttpStatus.OK).body(jobService.getJobById(id));
}catch (Exception e){
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getLocalizedMessage());
}
    }

    }
