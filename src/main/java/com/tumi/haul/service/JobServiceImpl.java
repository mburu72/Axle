package com.tumi.haul.service;

import com.tumi.haul.model.enums.JobStatus;
import com.tumi.haul.model.job.Job;
import com.tumi.haul.model.user.Client;
import com.tumi.haul.repository.ClientRepository;
import com.tumi.haul.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class JobServiceImpl implements JobService{
    private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);
    private final JobRepository jobRepository;
    private final ClientRepository clientRepository;

    public JobServiceImpl(JobRepository jobRepository, ClientRepository clientRepository) {
        this.jobRepository = jobRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public void createJob(Long clientId, Job job) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(()
                        -> new RuntimeException("Client not Found"));
        try{
            job.setOwner(client);
            job.setStatus(JobStatus.POSTED);
            jobRepository.save(job);
        }catch (Exception e){
            log.warn("error:", e);
        }
    }
    public Job updateJob(Long jobId, Long clientId, Job updatedJob){
        Job job = getJobById(jobId);
        if(!job.getOwner().getId().equals(clientId)){
            throw new RuntimeException("Unauthorized: Only the job's creator can update the job");
        }
        job.setCargoDetails(updatedJob.getCargoDetails());
        job.setPickupLocation(updatedJob.getPickupLocation());
        job.setDropOffLocation(updatedJob.getDropOffLocation());
        return jobRepository.save(job);
    }
    public Job acceptJob(Long jobId, Long clientId){
        Job job = getJobById(jobId);
        if(job.getStatus() != JobStatus.POSTED){
            throw new RuntimeException("Job already accepted");
        }
        Client client = clientRepository.findById(clientId)
                .orElseThrow(()
                -> new RuntimeException("Not found"));
        job.setOwner(client);
        job.setStatus(JobStatus.ACCEPTED);
        return jobRepository.save(job);
    }
    public Job completeJob(Long id, Long clientId){
        Job job = getJobById(id);
        log.info("client id in job service is: {}", clientId);
        log.info("job id in job service is: {}", id);
        log.info("job is: {}", job);
        if (!job.getOwner().getId().equals(clientId)){
            throw new RuntimeException("Unauthorized:Only the assigned hauler can complete the job");
        }
        if (job.getStatus()==JobStatus.COMPLETED){
            throw new RuntimeException("Job is already complete!");
        }
        log.info(job.toString());
        job.setStatus(JobStatus.COMPLETED);
        job.setCompletionDate(LocalDateTime.now());
        return jobRepository.save(job);
    }

    @Override
    public void deleteJob(Long idChar, Long clientId) {
        Job job = getJobById(idChar);
        if(!job.getOwner().getId().equals(clientId)){
            throw new RuntimeException("Unauthorized:Only the jobs creator can delete a job");
        }
   jobRepository.delete(job);
    }

    @Override
    public List<Job> getAllJobs() {
        return List.of();
    }

    @Override
    public Job getJobById(Long idChar) {
        Job job = jobRepository.findById(idChar)
                .orElseThrow(()
                ->new RuntimeException("Job Not found!"));
        return job;
    }

    @Override
    public Optional<Job> getJobByClient(Long idChar) {
        return Optional.empty();
    }
}
