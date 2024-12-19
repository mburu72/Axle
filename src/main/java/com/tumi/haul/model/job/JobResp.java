package com.tumi.haul.model.job;


import java.util.List;

public record JobResp (
        String id,
        String pickupLocation,
        String dropOffLocation,
        String budget,
        String cargoDetails,
        Boolean isFragile,
        Boolean isPerishable,
        String industry,
        String executionDate,
        String ownerDetails,
        String jobStatus,
        String creationDate,
        List<String> imageUrls


){

}