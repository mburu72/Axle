package com.tumi.haul.service;

import com.tumi.haul.model.job.Job;
import com.tumi.haul.model.user.Client;

import java.util.List;
import java.util.Optional;

public interface JobService {
    public void createJob(Long clientId, Job job);
    public Job acceptJob(Long jobId, Long clientId);
    public Job completeJob(Long id, Long clientId);
    public Job updateJob(Long jobId, Long clientId, Job updatedJob);
   // public Optional<Job>findByName(Name name);
    public void deleteJob(Long idChar, Long clientId);
    public List<Job> getAllJobs();
    public Job getJobById(Long idChar);
    public Optional<Job>getJobByClient(Long idChar);
    //public Optional<Job>getJobByLocation();
}
