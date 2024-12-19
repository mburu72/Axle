package com.tumi.haul.model.bid;


import com.tumi.haul.model.converters.AmountConverter;
import com.tumi.haul.model.converters.CreationDateConverter;
import com.tumi.haul.model.enums.QuoteStatus;
import com.tumi.haul.model.job.Job;
import com.tumi.haul.model.primitives.Amount;
import com.tumi.haul.model.primitives.CreationDate;
import com.tumi.haul.model.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "quotes")
public class Quote implements Serializable {
    @Getter
    @Id
    private String id;
    @PrePersist
    public void generateId(){
        if (this.id == null){
            this.id = "q_" + UUID.randomUUID().toString().substring(0, 8).toLowerCase();
        }
    }
    @ManyToOne
    private Job job;
    @ManyToOne
    private User hauler;
    private String imageName;
    private String imageType;
    private String imageUrl;
    @Convert(converter = AmountConverter.class)
    private Amount quoteAmount;
    @Enumerated(EnumType.STRING)
    private QuoteStatus status= QuoteStatus.PENDING;
    @CreationTimestamp
    @Column(updatable = false)
    @Convert(converter = CreationDateConverter.class)
    protected CreationDate creationDate;
    protected String executionDate;

    private Quote(

            final Amount quoteAmount,
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
