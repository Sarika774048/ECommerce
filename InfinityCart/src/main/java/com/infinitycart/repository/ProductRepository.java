package com.infinitycart.repository;

import com.infinitycart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findCategoryByCategoryId(String categoryId);
}
