package com.tumi.haul.repository;

import com.tumi.haul.model.job.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface JobRepository extends JpaRepository<Job, String> {
 List<Job> findByOwnerId(String ownerId);
}
