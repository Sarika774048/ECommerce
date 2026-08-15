package com.infinitycart.service.impl;

import com.infinitycart.model.Product;
import com.infinitycart.model.Review;
import com.infinitycart.model.User;
import com.infinitycart.repository.ReviewRepository;
import com.infinitycart.request.CreateReviewRequest;
import com.infinitycart.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public Review createReview(
            CreateReviewRequest req,
            User user,
            Product product
    ) {

        Review review = new Review();

        review.setUser(user);
        review.setProduct(product);
        review.setReviewText(req.getReviewText());
        review.setRating(Double.parseDouble(req.getReviewRating()));
        review.setProductImages(req.getProductImage());

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewByProductId(Long productId) {

        return reviewRepository.findByProductId(productId);
    }

    @Override
    public Review updateReview(
            Long reviewId,
            String reviewText,
            double rating,
            Long userId
    ) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new RuntimeException("Review not found"));

        // Check review ownership
        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException(
                    "You don't have permission to update this review"
            );
        }

        review.setReviewText(reviewText);
        review.setRating(rating);

        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(
            Long reviewId,
            Long userId
    ) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new RuntimeException("Review not found"));

        // Check review ownership
        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException(
                    "You don't have permission to delete this review"
            );
        }

        reviewRepository.delete(review);
    }

    @Override
    public Review getReviewById(Long reviewId) {

        return reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new RuntimeException("Review not found"));
    }
}