package com.infinitycart.controller;

import com.infinitycart.model.Deal;
import com.infinitycart.response.ApiResponse;
import com.infinitycart.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deals")
public class DealController {
    private final DealService dealService;

    @PostMapping()
    public ResponseEntity<Deal> createDeals(){
        Deal deal = dealService.createDeal(new Deal());
        return ResponseEntity.ok(deal);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Deal> updateDeal(
            @PathVariable Long id,
            @RequestBody Deal deal
    ) throws Exception {
        Deal updateDeal = dealService.updateDeal(deal, id);
        return ResponseEntity.ok(updateDeal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDeals(
            @PathVariable Long id
    ){
        dealService.deleteDeal(id);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Successfully deleted deal");

        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
    }

}
