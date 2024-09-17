package com.tumi.haul.model.rating;

import com.tumi.haul.model.job.Job;
import com.tumi.haul.model.user.Client;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class Rating implements Serializable {
    @Id
    private Long id;
    @ManyToOne
    private Job job;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Client hauler;
    private Integer rating;
    private String review;
    private Date ratingDate = new Date();

}
