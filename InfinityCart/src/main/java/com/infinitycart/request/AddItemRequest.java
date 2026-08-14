package com.infinitycart.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
public class AddItemRequest {

    private Long productId;
    private String size;
    private int quantity;

}
