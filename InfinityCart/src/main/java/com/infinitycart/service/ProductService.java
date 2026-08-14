package com.infinitycart.service;

import com.infinitycart.model.Product;
import com.infinitycart.model.Seller;
import com.infinitycart.request.CreateProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    Product createProduct(CreateProductRequest req, Seller seller);
    void deleteProduct(Long productId);
    Product updateProduct(Long productId, Product product) throws Exception;
    Product findProductById(Long productId) throws Exception;
    List<Product> searchProducts(String query);
    Page<Product> getAllProducts(
       String category,
       String brand,
       String colors,
       String sizes,
       Integer minPrice,
       Integer maxPrice,
       Integer minDiscount,
       String sort,
       String stock,
       Integer pageNumber
    );

    List<Product> getProductsBySellerId(Long sellerId);
}
