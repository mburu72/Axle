package com.tumi.haul.service;

import com.tumi.haul.model.job.Job;
import com.tumi.haul.model.job.JobResp;
import com.tumi.haul.model.primitives.UserId;

import com.tumi.haul.model.user.Client;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;


public interface JobService {
    String createJob(String clientId, Job job, List<MultipartFile> image) throws IOException;
    String addJobnClient(Client client, Job job, List<MultipartFile> image) throws Exception;
    // public Job completeJob(Long id, UUID clientId);
    public Job updateJob(String jobId, String clientId, Job updatedJob);
    public Job updateJobStatus(String jobId, Job updatedJob);
    public void deleteJob(String jobId, String clientId);
    public List<JobResp> getAllJobs() throws IOException;
    public JobResp getJobById(String jobId) throws IOException;
    public List<JobResp>getJobByOwnerId(String clientId) throws IOException;

    //public Optional<Job>getJobByLocation();
}
