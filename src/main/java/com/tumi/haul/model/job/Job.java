package com.tumi.haul.model.job;

import com.tumi.haul.model.converters.PriceConverter;
import com.tumi.haul.model.enums.JobStatus;
import com.tumi.haul.model.primitives.Price;
import com.tumi.haul.model.user.Client;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
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
    /*
    @ManyToOne
    private Client hauler;*/
    private String pickupLocation;
    private String dropOffLocation;
    private String cargoDetails;
   // @Convert(converter = PriceConverter.class)
    private String price;
    @Enumerated(EnumType.STRING)
    private JobStatus status= JobStatus.POSTED;
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime creationDate;

    private LocalDateTime completionDate;
private Job(
        final Client owner,
        String pickupLocation,
        String dropOffLocation,
        String cargoDetails,
        String price,
        final LocalDateTime creationDate
){
    this.owner=notNull(owner);
    this.pickupLocation=notNull(pickupLocation);
    this.dropOffLocation=notNull(dropOffLocation);
    this.cargoDetails=notNull(cargoDetails);
    this.price=price;
    this.creationDate=creationDate;
    this.completionDate=null;
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
