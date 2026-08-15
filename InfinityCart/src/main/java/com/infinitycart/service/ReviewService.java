package com.infinitycart.service;

import com.infinitycart.model.Product;
import com.infinitycart.model.Review;
import com.infinitycart.model.User;
import com.infinitycart.request.CreateReviewRequest;

import java.util.List;

public interface ReviewService {
    Review createReview(CreateReviewRequest req, User user, Product prodcut);
    List<Review> getReviewByProductId(Long productId);
    Review updateReview(Long reviewId, String reviewText, double rating, Long userId);
    void deleteReview(Long reviewId, Long userId);
    Review getReviewById(Long reviewId);
}
