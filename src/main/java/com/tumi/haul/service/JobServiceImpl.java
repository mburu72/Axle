package com.tumi.haul.service;

import com.tumi.haul.model.enums.JobStatus;
import com.tumi.haul.model.job.ImageInfo;
import com.tumi.haul.model.job.Job;
import com.tumi.haul.model.job.JobResp;
import com.tumi.haul.model.primitives.Amount;
import com.tumi.haul.model.user.Client;
import com.tumi.haul.repository.ClientRepository;
import com.tumi.haul.repository.JobRepository;
import com.tumi.haul.service.emailservice.EmailService;
import com.tumi.haul.service.otpservice.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService{
    @Value("${image.storage.path}")
    private String imageStoragePath;
    private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);
    private final JobRepository jobRepository;
    private final ClientRepository clientRepository;
    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;

    public JobServiceImpl(JobRepository jobRepository, ClientRepository clientRepository, UserService userService, OtpService otpService, EmailService emailService) {
        this.jobRepository = jobRepository;
        this.clientRepository = clientRepository;
        this.userService = userService;

        this.otpService = otpService;
        this.emailService = emailService;
    }


    public Job updateJob(String jobId, String clientId, Job updatedJob){
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

    @Override
    public Job updateJobStatus(String jobId, Job updatedJob) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(()
                        ->new RuntimeException("Job Not found!"));
        job.setStatus(updatedJob.getStatus());
        return null;
    }

    /*   public Job completeJob(Long id, Long clientId){
           Job job = jobRepository.findById(id)
                   .orElseThrow(()
                           ->new RuntimeException("Job Not found!"));
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
   */
    @Override
    public void deleteJob(String jobId, String clientId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(()
                        ->new RuntimeException("Job Not found!"));
        if(!job.getOwner().getId().equals(clientId)){
            throw new RuntimeException("Unauthorized:Only the jobs creator can delete a job");
        }
        jobRepository.delete(job);
    }

    @Override
    public List<JobResp> getAllJobs() throws IOException {
        List<Job> jobList = jobRepository.findAll();
        JobResp jobResp ;
        List<JobResp> jobs = new ArrayList<>();

        for (Job job:jobList){
            String budgetString = Optional.ofNullable(job.getBudget())
                    .map(Amount::getValue) // Assuming getValue() returns BigDecimal
                    .map(BigDecimal::toString)
                    .orElse("No budget"); // Default value when budget is null
            List<String> imageUrls = job.getImages().stream()
                    .map(imageInfo -> ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/api/images/")
                            .path(imageInfo.getImageName()) // Image name from ImageInfo object
                            .toUriString())
                    .collect(Collectors.toList());

            jobResp = new JobResp(
                    job.getId(),
                    job.getPickupLocation().getValue(),
                    job.getDropOffLocation().getValue(),
                    budgetString,
                    job.getCargoDetails().getValue(),
                    job.getIsFragile(),
                    job.getIsPerishable(),
                    job.getIndustry(),
                    job.getExecutionDate(),
                    job.getOwner().getFirstName().getValue() + job.getOwner().getLastName().getValue(),
                    job.getStatus().toString(),
                    job.getCreationDate().getValue().toString(),
                    imageUrls
            );
            jobs.add(jobResp);
        }
        return  jobs;
    }

    @Override
    public JobResp getJobById(String jobId) throws IOException {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(()
                        ->new RuntimeException("Job Not found!"));
        List<String> imageUrls = job.getImages().stream()
                .map(imageInfo -> ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/images/")
                        .path(imageInfo.getImageName()) // Image name from ImageInfo object
                        .toUriString())
                .collect(Collectors.toList());
        String budgetString = Optional.ofNullable(job.getBudget())
                .map(Amount::getValue) // Assuming getValue() returns BigDecimal
                .map(BigDecimal::toString)
                .orElse("No budget"); // Default value when budget is null


        return new JobResp(
                job.getId(),
                job.getPickupLocation().getValue(),
                job.getDropOffLocation().getValue(),
                budgetString,
                job.getCargoDetails().getValue(),
                job.getIsFragile(),
                job.getIsPerishable(),
                job.getIndustry(),
                job.getExecutionDate(),
                job.getOwner().getFirstName().getValue() + " " +  job.getOwner().getLastName().getValue(),
                job.getStatus().toString(),
                job.getCreationDate().getValue().toString(),
                imageUrls
        );
    }

    @Override
    public List<JobResp>getJobByOwnerId(String clientId) throws IOException {
        List<Job> jobList = jobRepository.findByOwnerId(clientId);
        JobResp jobResp ;
        List<JobResp> jobs = new ArrayList<>();
        for (Job job:jobList){
            String budgetString = Optional.ofNullable(job.getBudget())
                    .map(Amount::getValue) // Assuming getValue() returns BigDecimal
                    .map(BigDecimal::toString)
                    .orElse("No budget"); // Default value when budget is null
            List<String> imageUrls = job.getImages().stream()
                    .map(imageInfo -> ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/api/images/")
                            .path(imageInfo.getImageName()) // Image name from ImageInfo object
                            .toUriString())
                    .collect(Collectors.toList());
            jobResp = new JobResp(
                    job.getId(),
                    job.getPickupLocation().getValue(),
                    job.getDropOffLocation().getValue(),
                    budgetString,
                    job.getCargoDetails().getValue(),
                    job.getIsFragile(),
                    job.getIsPerishable(),
                    job.getIndustry(),
                    job.getExecutionDate(),
                    job.getOwner().getFirstName().getValue() + job.getOwner().getLastName().getValue(),
                    job.getStatus().toString(),
                    job.getCreationDate().getValue().toString(),
                    imageUrls
            );
            jobs.add(jobResp);

        }
        return  jobs;
    }

    @Override
    public String addJobnClient(Client client, Job job, List<MultipartFile> image) throws Exception {
    Client existingClient = clientRepository
            .findByEmailOrPhoneNumber(client.getEmail(),client.getPhoneNumber())
            .orElse(null);
    if (existingClient == null){
        existingClient = client;
        userService.createClient(existingClient);
        return createJob(existingClient.getId(), job, image);
    }else{
        throw new IllegalStateException("Please login to create a delivery");
    }

    }
    @Override
    public String createJob(String clientId, Job job, List<MultipartFile> images) throws IOException {
        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("At least one image file is required.");
        }

        // Find the client by ID
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // List to hold the image information
        List<ImageInfo> imageInfos = new ArrayList<>();

        // Process each image in the list
        for (MultipartFile image : images) {
            String originalFileName = image.getOriginalFilename();
            if (originalFileName == null || originalFileName.isEmpty()) {
                throw new IllegalArgumentException("Invalid image file.");
            }

            // Create a unique file name using UUID
            String imageName = UUID.randomUUID().toString() + "_" + originalFileName;

            // Define image storage path
            Path imageUrl = Path.of(imageStoragePath).resolve(imageName);
            Path imageDir = imageUrl.getParent();

            // Ensure the directory exists
            if (!Files.exists(imageDir)) {
                try {
                    Files.createDirectories(imageDir);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to create image directory", e);
                }
            }

            // Save the image to the file system
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, imageUrl, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save image", e);
            }

            // Create ImageInfo object with the image name and MIME type
            String imageType = image.getContentType(); // Get MIME type (e.g., "image/jpeg")
            ImageInfo imageInfo = new ImageInfo(imageUrl.toString(),imageName, imageType);

            // Add the image info to the list
            imageInfos.add(imageInfo);
        }

        // Set the image information (List of ImageInfo objects) on the job
        job.setImages(imageInfos);  // Assuming the Job has a setImages method to handle List<ImageInfo>
        job.setOwner(client);
        job.setStatus(JobStatus.POSTED);

        // Save the job
        jobRepository.save(job);

        return "Job created with " + images.size() + " image(s)!";
    }

}
