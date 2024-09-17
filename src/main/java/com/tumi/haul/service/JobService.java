package com.tumi.haul.service;

import com.tumi.haul.model.job.Job;
import com.tumi.haul.model.job.JobResp;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface JobService {
    String addJob(Long clientId, Job job, MultipartFile image) throws IOException;
    public Job completeJob(Long id, Long clientId);
    public Job updateJob(Long jobId, Long clientId, Job updatedJob);
    public void deleteJob(Long idChar, Long clientId);
    public List<Job> getAllJobs();
    public JobResp getJobById(Long idChar);
    public List<JobResp>getJobByOwnerId(Long clientId);

    //public Optional<Job>getJobByLocation();
}
