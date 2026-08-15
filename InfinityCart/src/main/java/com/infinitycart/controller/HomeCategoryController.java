package com.infinitycart.controller;

import com.infinitycart.model.Home;
import com.infinitycart.model.HomeCategory;
import com.infinitycart.service.HomeCategoryService;
import com.infinitycart.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HomeCategoryController {
    private final HomeCategoryService homeCategoryService;
    private final HomeService homeService;

    @PostMapping("/home/categories")
    public ResponseEntity<Home> createHomeCategories(
            @RequestBody List<HomeCategory> homeCategories
            ){
        List<HomeCategory> categories = homeCategoryService.createCategories(homeCategories);
        Home home = homeService.createHomePageData(homeCategories);
        return new ResponseEntity<>(home, HttpStatus.OK);
    }

    @GetMapping("/admin/home-category")
    public ResponseEntity<List<HomeCategory>> getHomeCategory(){
        List<HomeCategory> categories = homeCategoryService.getAllHomeCategories();
        return ResponseEntity.ok(categories);
    }

    @PatchMapping("/admin/home-category/{id}")
    public ResponseEntity<HomeCategory> updateHomeCategory(
            @PathVariable Long id,
            @RequestBody HomeCategory homeCategory
    ) throws Exception {
        HomeCategory updatedCategory = homeCategoryService.updateHomeCategory(homeCategory, id);
        return ResponseEntity.ok(updatedCategory);
    }

}
