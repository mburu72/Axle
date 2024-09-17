package com.tumi.haul.service;

import com.tumi.haul.model.rating.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingService {
    public Optional<Rating> createRating(Rating rating);
    public void deleteRating(Long idChar);
    public List<Rating> getAllRatings();
    public Optional<Rating>getRatingById(Long idChar);

}
