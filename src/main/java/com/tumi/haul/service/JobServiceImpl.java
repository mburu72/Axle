package com.tumi.haul.service;

import com.tumi.haul.model.enums.JobStatus;
import com.tumi.haul.model.job.Job;
import com.tumi.haul.model.job.JobResp;
import com.tumi.haul.model.user.Client;
import com.tumi.haul.repository.ClientRepository;
import com.tumi.haul.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class JobServiceImpl implements JobService{
    private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);
    private final JobRepository jobRepository;
    private final ClientRepository clientRepository;

    public JobServiceImpl(JobRepository jobRepository, ClientRepository clientRepository) {
        this.jobRepository = jobRepository;
        this.clientRepository = clientRepository;
    }

    public Job updateJob(Long jobId, Long clientId, Job updatedJob){
        Job job = jobRepository.findById(jobId)
                .orElseThrow(()
                        ->new RuntimeException("Job Not found!"));
        if(!job.getOwner().getId().equals(clientId)){
            throw new RuntimeException("Unauthorized: Only the job's creator can update the job");
        }
        job.setCargoDetails(updatedJob.getCargoDetails());
        job.setPickupLocation(updatedJob.getPickupLocation());
        job.setDropOffLocation(updatedJob.getDropOffLocation());
        return jobRepository.save(job);
    }

    public Job completeJob(Long id, Long clientId){
        Job job = jobRepository.findById(id)
                .orElseThrow(()
                        ->new RuntimeException("Job Not found!"));
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
        return jobRepository.save(job);
    }

    @Override
    public void deleteJob(Long idChar, Long clientId) {
        Job job = jobRepository.findById(idChar)
                .orElseThrow(()
                        ->new RuntimeException("Job Not found!"));
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
    public JobResp getJobById(Long idChar) {
        Job job = jobRepository.findById(idChar)
                .orElseThrow(()
                        ->new RuntimeException("Job Not found!"));
        JobResp jobResp = new JobResp(
                job.getId().toString(),
                job.getPickupLocation().getValue(),
                job.getDropOffLocation().getValue(),
                job.getCargoDetails().getValue(),
                job.getExecutionDate(),
                job.getOwner().getFirstName().getValue() + " " +  job.getOwner().getLastName().getValue(),
                job.getStatus().toString(),
                job.getCreationDate().getValue().toString(),
                job.getImageName(),
                job.getImageType(),
                job.getImageUrl()
        );
log.info("fetched: {}", job);
return jobResp;
    }

    @Override
    public List<JobResp>getJobByOwnerId(Long clientId) {
        List<Job> jobList = jobRepository.findByOwnerId(clientId);
        JobResp jobResp ;
        List<JobResp> jobs = new ArrayList<>();
        for (Job job:jobList){
            jobResp = new JobResp(
                    job.getId().toString(),
                    job.getPickupLocation().getValue(),
                    job.getDropOffLocation().getValue(),
                    job.getCargoDetails().getValue(),
                    job.getExecutionDate().toString(),
                    job.getOwner().getFirstName().getValue() + job.getOwner().getLastName().getValue(),
                    job.getStatus().toString(),
                    job.getCreationDate().getValue().toString(),
                    job.getImageName(),
                    job.getImageType(),
                    job.getImageUrl()
            );
            jobs.add(jobResp);
        }
        return  jobs;
    }


    @Override
    public String addJob(Long clientId, Job job, MultipartFile image) throws IOException {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(()
                        -> new RuntimeException("Client not Found"));
        try{
            String imageName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
            Path imageUrl = Path.of("uploads/images").resolve(imageName);
            Path imageDir = imageUrl.getParent();
            if (!Files.exists(imageDir)) {
                Files.createDirectories(imageDir);
            }
            Files.copy(image.getInputStream(), imageUrl, StandardCopyOption.REPLACE_EXISTING);

            job.setImageName(imageName);
            job.setImageType(image.getContentType());
            job.setImageUrl(imageUrl.toString());

            job.setOwner(client);
            log.info("created by : {}",job.getOwner().getFirstName().toString());
            job.setStatus(JobStatus.POSTED);
            jobRepository.save(job);
            return "JobCreated!";
        }catch (Exception e){
            log.warn("error:", e);
            return "Failed!";
        }
    }
    public ResponseEntity<Resource> serveImage(Long id){
        final Path rootLocation = Paths.get("uploads/images");
        Job job = jobRepository.findById(id)
                .orElseThrow(()
                        ->new RuntimeException("Job Not found!"));
        String imageName = job.getImageName();
        Path filePath = rootLocation.resolve(imageName);
        Resource resource;
        try{
            resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
