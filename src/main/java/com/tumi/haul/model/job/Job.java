package com.tumi.haul.model.job;

import com.tumi.haul.model.bid.Quote;
import com.tumi.haul.model.converters.AmountConverter;
import com.tumi.haul.model.converters.CreationDateConverter;
import com.tumi.haul.model.converters.JobDescriptionConverter;
import com.tumi.haul.model.converters.LocationConverter;
import com.tumi.haul.model.enums.JobStatus;
import com.tumi.haul.model.primitives.Amount;
import com.tumi.haul.model.primitives.CreationDate;
import com.tumi.haul.model.primitives.JobDescription;
import com.tumi.haul.model.primitives.Location;
import com.tumi.haul.model.user.Client;
import com.tumi.haul.model.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.apache.commons.lang3.Validate.notNull;

@Data
@Entity
@NoArgsConstructor
@Table(name = "jobs")
public class Job implements Serializable {
    @Getter
    @Id
    private String id;
    @PrePersist
    public void generateId(){
        if (this.id == null){
            this.id = "j_" + UUID.randomUUID().toString().substring(0, 8).toLowerCase();
        }
    }
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Client owner;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "job", cascade = CascadeType.ALL)
    private List<Quote> quote;
    @Convert(converter = LocationConverter.class)
    private Location pickupLocation;
    @Convert(converter = LocationConverter.class)
    private Location dropOffLocation;
    @Convert(converter = AmountConverter.class)
    private Amount budget = new Amount(new BigDecimal(0));
    @Convert(converter = JobDescriptionConverter.class)
    private JobDescription cargoDetails;
    private Boolean isFragile;
    private Boolean isPerishable;
    private String industry;
    @ElementCollection
    @CollectionTable(name = "job_images", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "image_info")
    private List<ImageInfo> images;
    @Enumerated(EnumType.STRING)
    private JobStatus status= JobStatus.POSTED;
    @CreationTimestamp
    @Column(updatable = false)
    @Convert(converter = CreationDateConverter.class)
    protected CreationDate creationDate;


    protected String executionDate;
    private Job(

            final Location pickupLocation,
            final Location dropOffLocation,
            final JobDescription cargoDetails,
            final String industry,
            List<ImageInfo> images,
            final CreationDate creationDate,
            final String executionDate
    ){

        this.pickupLocation=notNull(pickupLocation);
        this.dropOffLocation=notNull(dropOffLocation);
        this.cargoDetails=notNull(cargoDetails);
        this.industry=notNull(industry);
        this.images = images;
        this.creationDate=creationDate;
        this.executionDate=notNull(executionDate);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return id.equals(job.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
