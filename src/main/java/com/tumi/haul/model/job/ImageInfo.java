package com.tumi.haul.model.job;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ImageInfo {
    private String imageUrl;
    private String imageName;
    private String imageType;
}
