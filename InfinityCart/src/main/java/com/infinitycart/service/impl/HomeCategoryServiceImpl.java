package com.infinitycart.service.impl;

import com.infinitycart.model.HomeCategory;
import com.infinitycart.repository.HomeCategoryRepository;
import com.infinitycart.service.HomeCategoryService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeCategoryServiceImpl implements HomeCategoryService {

    private final HomeCategoryRepository homeCategoryRepository;

    @Override
    public HomeCategory createHomeCategory(HomeCategory homeCategory) {
        return homeCategoryRepository.save(homeCategory);
    }

    @Override
    public List<HomeCategory> createCategories(List<HomeCategory> homeCategories) {
        if(homeCategoryRepository.findAll().isEmpty()){
            return homeCategoryRepository.saveAll(homeCategories);
        }
        return homeCategoryRepository.findAll();

    }

    @Override
    public HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) throws Exception {
        HomeCategory existingCategory = homeCategoryRepository.findById(id).orElseThrow( () -> new Exception("Category Not found"));

        if(homeCategory.getImage() != null){
            existingCategory.setImage(homeCategory.getImage());
        }
        if(homeCategory.getCategoryId() != null){
            existingCategory.setCategoryId(existingCategory.getCategoryId());
        }
        return homeCategoryRepository.save(existingCategory);
    }

    @Override
    public List<HomeCategory> getAllHomeCategories() {
        return homeCategoryRepository.findAll();
    }
}
