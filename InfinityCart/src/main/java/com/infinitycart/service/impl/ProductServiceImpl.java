package com.infinitycart.service.impl;

import com.infinitycart.model.Product;
import com.infinitycart.model.Seller;
import com.infinitycart.repository.CategoryRepository;
import com.infinitycart.repository.ProductRepository;
import com.infinitycart.request.CreateProductRequest;
import com.infinitycart.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product createProduct(CreateProductRequest req, Seller seller) {



        return null;
    }

    @Override
    public void deleteProduct(Long productId) {
       productRepository.deleteById(productId);
    }

    @Override
    public Product updateProduct(Long productId, Product product) {
        return null;
    }

    @Override
    public Product findProductById(Long productId) {
        return null;
    }

    @Override
    public List<Product> searchProducts() {
        return List.of();
    }

    @Override
    public Page<Product> getAllProducts(String category, String brand, String colors, String sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber) {
        return null;
    }

    @Override
    public List<Product> getProductsBySellerId(Long sellerId) {
        return List.of();
    }
}
