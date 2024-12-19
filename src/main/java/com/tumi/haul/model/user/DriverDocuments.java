package com.tumi.haul.model.user;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class DriverDocuments {
    private String idCardFront;
    private String idCardBack;
    private String licenseFront;
    private String licenseBack;
    private String passportPhoto;
}
