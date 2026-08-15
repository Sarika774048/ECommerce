package com.infinitycart.service;

import com.infinitycart.model.Home;
import com.infinitycart.model.HomeCategory;

import java.util.List;

public interface HomeService {
    Home createHomePageData(List<HomeCategory> allCategories);
}
