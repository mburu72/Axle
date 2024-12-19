package com.tumi.haul.model.user;

import com.tumi.haul.model.converters.CreationDateConverter;
import com.tumi.haul.model.converters.RoleConverter;
import com.tumi.haul.model.enums.Roles;
import com.tumi.haul.model.job.Job;
import com.tumi.haul.model.primitives.CreationDate;
import com.tumi.haul.model.primitives.Email;
import com.tumi.haul.model.primitives.Name;
import com.tumi.haul.model.primitives.PhoneNumber;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor(force = true)
public class Client extends BaseUser implements Serializable {
    @Getter
    @Id
    private String id;
    @PrePersist
    public void generateId(){
        if (this.id == null){
            this.id = "c_" + UUID.randomUUID().toString().substring(0, 8).toLowerCase();
        }
    }
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Job> job;
    @Convert(converter = RoleConverter.class)
    private Roles role= Roles.CLIENT;
    private String password = null;
    @CreationTimestamp
    @Column(updatable = false)
    @Convert(converter = CreationDateConverter.class)
    protected CreationDate creationDate;
    private Client(final Name firstName,
                 final Name lastName,
                 final PhoneNumber phoneNumber,
                 final Email email,
                 final String password,
                 final boolean verified,
                 final Roles role,
                 final CreationDate creationDate) {
        super(firstName, lastName, phoneNumber,creationDate, email, password, verified);
        this.creationDate = creationDate;
        this.role = role;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id.equals(client.id);
    }

    public String toString(){
        return "Client{" +
                "id='" + id + "', name=" + getFirstName() + " " + getLastName() +
                ", phoneNumber=" + getPhoneNumber() + ", email=" + getEmail() + ", creationDate=" + creationDate + "}";
    }

}
