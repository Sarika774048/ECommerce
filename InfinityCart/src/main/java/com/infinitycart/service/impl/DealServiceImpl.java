package com.infinitycart.service.impl;

import com.infinitycart.model.Deal;
import com.infinitycart.model.HomeCategory;
import com.infinitycart.repository.DealRepository;
import com.infinitycart.repository.HomeCategoryRepository;
import com.infinitycart.service.DealService;
import com.infinitycart.service.HomeCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DealServiceImpl implements DealService {

    private final DealRepository dealRepository;
    private final HomeCategoryRepository homeCategoryRepository;
    @Override
    public List<Deal> getDeals() {
        return dealRepository.findAll();
    }

    @Override
    public Deal createDeal(Deal deal) {

        HomeCategory homeCategory = homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);
        Deal newDeal = dealRepository.save(deal);
        newDeal.setCategory(homeCategory);
        newDeal.setDiscount(deal.getDiscount());
        return dealRepository.save(newDeal);
    }

    @Override
    public Deal updateDeal(Deal deal, Long dealId) throws Exception {
        Deal existingDeal = dealRepository.findById(dealId).orElse(null);
        HomeCategory category = homeCategoryRepository.findById(deal.getCategory().getId()).orElseThrow( ()-> new Exception("Deal not Found"));

        if(existingDeal != null){
            if(deal.getDiscount() != null){
                existingDeal.setDiscount(deal.getDiscount());
            }
            if(category != null){
                existingDeal.setCategory(category);
            }
            return dealRepository.save(existingDeal);
        }
        throw new Exception("Deal not Found");
    }


    @Override
    public void deleteDeal(Long id) {
        Deal deal = dealRepository.findById(id).orElse(null);
        dealRepository.delete(deal);
    }
}
