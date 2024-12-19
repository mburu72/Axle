package com.tumi.haul.model.refresh;

import com.tumi.haul.model.user.BaseUser;
import com.tumi.haul.model.user.Client;
import com.tumi.haul.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @Getter
    @Id
    private String id;
    @PrePersist
    public void generateId(){
        if (this.id == null){
            this.id = "r_" + UUID.randomUUID().toString().substring(0, 8).toLowerCase();
        }
    }
    private String token;
    private Instant expirationDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public BaseUser getAssociatedUser() {
        return (user != null) ? user : client;
    }
}