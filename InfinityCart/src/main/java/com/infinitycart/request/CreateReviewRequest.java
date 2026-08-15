package com.infinitycart.request;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class CreateReviewRequest {

    private String reviewText;
    private String reviewRating;
    private List<String> productImage;
}
