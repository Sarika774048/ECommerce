package com.infinitycart.service.impl;

import com.infinitycart.domain.HomeCategorySection;
import com.infinitycart.model.Deal;
import com.infinitycart.model.Home;
import com.infinitycart.model.HomeCategory;
import com.infinitycart.repository.DealRepository;
import com.infinitycart.repository.HomeCategoryRepository;
import com.infinitycart.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HomeServiceImpl implements HomeService {

    private final DealRepository dealRepository;
    private final HomeCategoryRepository homeCategoryRepository;

    @Override
    public Home createHomePageData(List<HomeCategory> allCategories) {

        List<HomeCategory> electricCategories = allCategories.stream()
                .filter(category ->
                        category.getSection() == HomeCategorySection.ELECTRIC_CATEGORIES
                )
                .collect(Collectors.toList());

        List<HomeCategory> gridCategories = allCategories.stream()
                .filter(category ->
                        category.getSection() == HomeCategorySection.GRID
                )
                .collect(Collectors.toList());

        List<HomeCategory> shopByCategories = allCategories.stream()
                .filter(category ->
                        category.getSection() == HomeCategorySection.SHOP_BY_CATEGORIES
                )
                .collect(Collectors.toList());

        List<HomeCategory> deals = allCategories.stream()
                .filter(category ->
                        category.getSection() == HomeCategorySection.DEALS
                )
                .collect(Collectors.toList());

        List<Deal> createdDeals = new ArrayList<>();

        if(dealRepository.findAll().isEmpty()){
            List<Deal> deal = allCategories.stream()
                    .filter(category -> category.getSection() == HomeCategorySection.DEALS)
                    .map(category -> new Deal())
                    .collect(Collectors.toList());
            createdDeals = dealRepository.saveAll(deal);
        }
        else{
            createdDeals = dealRepository.findAll();
        }

        Home home = new Home();

        home.setElectricCategories(electricCategories);
        home.setGrid(gridCategories);
        home.setShopByCategories(shopByCategories);
        home.setDeals(createdDeals);

        return home;
    }
}