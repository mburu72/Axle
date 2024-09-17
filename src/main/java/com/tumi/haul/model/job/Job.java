package com.tumi.haul.model.job;

import com.tumi.haul.model.bid.Quote;
import com.tumi.haul.model.converters.CreationDateConverter;
import com.tumi.haul.model.converters.JobDescriptionConverter;
import com.tumi.haul.model.converters.LocationConverter;
import com.tumi.haul.model.enums.JobStatus;
import com.tumi.haul.model.primitives.CreationDate;
import com.tumi.haul.model.primitives.JobDescription;
import com.tumi.haul.model.primitives.Location;
import com.tumi.haul.model.user.Client;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.notNull;

@Data
@Entity
@ToString
@NoArgsConstructor
@Table(name = "jobs")
public class Job implements Serializable {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Client owner;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "job", cascade = CascadeType.ALL)
    private List<Quote> quote;
/*
    @ManyToOne
    private Client hauler;*/
    @Convert(converter = LocationConverter.class)
    private Location pickupLocation;
    @Convert(converter = LocationConverter.class)
    private Location dropOffLocation;
    @Convert(converter = JobDescriptionConverter.class)
    private JobDescription cargoDetails;

   private String imageName;
   private String imageType;
   private String imageUrl;
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
        final String imageName,
        final String imageType,
        final String imageUrl,
        final CreationDate creationDate,
       final String executionDate
){

    this.pickupLocation=notNull(pickupLocation);
    this.dropOffLocation=notNull(dropOffLocation);
    this.cargoDetails=notNull(cargoDetails);
    this.imageName = imageName;
    this.imageType = imageType;
    this.imageUrl = imageUrl;
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
