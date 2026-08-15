package com.infinitycart.controller;

import com.infinitycart.model.Review;
import com.infinitycart.model.User;
import com.infinitycart.request.CreateReviewRequest;
import com.infinitycart.response.ApiResponse;
import com.infinitycart.service.ProductService;
import com.infinitycart.service.ReviewService;
import com.infinitycart.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<Review>> getReviewByProductId(@PathVariable Long productId){
        List<Review> reviews =  reviewService.getReviewByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<Review> createReview(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long productId, @RequestBody CreateReviewRequest request) throws Exception {
        Review review = reviewService.createReview(request, userService.findUserByJwtToken(jwt), productService.findProductById(productId));
        return ResponseEntity.ok(review);
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @RequestBody CreateReviewRequest req,
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Review review = reviewService.updateReview(
          reviewId,
          req.getReviewText(),
                Double.parseDouble(req.getReviewRating()),
          user.getId()
        );
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        reviewService.deleteReview(reviewId, user.getId());
        ApiResponse res = new ApiResponse();
        res.setMessage("Successfully deleted review");
        return ResponseEntity.ok(res);

    }


}
