package com.tumi.haul.model.bid;


import com.tumi.haul.model.converters.CreationDateConverter;
import com.tumi.haul.model.enums.QuoteStatus;
import com.tumi.haul.model.job.Job;
import com.tumi.haul.model.primitives.CreationDate;
import com.tumi.haul.model.user.Hauler;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Objects;

@Data
@Entity
@ToString
@NoArgsConstructor
@Table(name = "quotes")
public class Quote implements Serializable {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Job job;
    @ManyToOne
    private Hauler hauler;
    private String imageName;
    private String imageType;
    private String imageUrl;
    private String quoteAmount;
   @Enumerated(EnumType.STRING)
    private QuoteStatus status= QuoteStatus.PENDING;
    @CreationTimestamp
    @Column(updatable = false)
    @Convert(converter = CreationDateConverter.class)
    protected CreationDate creationDate;
    protected String executionDate;

    private Quote(

            final String quoteAmount,
            final String executionDate,
            final String imageName,
            final String imageType,
            final String imageUrl
    ){
        this.quoteAmount = quoteAmount;
        this.executionDate = executionDate;
        this.imageName = imageName;
        this.imageType = imageType;
        this.imageUrl = imageUrl;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quote quote = (Quote) o;
        return id.equals(quote.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
