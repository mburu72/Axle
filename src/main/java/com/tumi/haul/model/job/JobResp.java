package com.tumi.haul.model.job;


public record JobResp (
        String id,
        String pickupLocation,
        String dropOffLocation,
        String cargoDetails,
        String executionDate,
        String ownerDetails,
        String jobStatus,
        String creationDate,
        String imageName,
        String imageType,
        String imageUrl


){

}