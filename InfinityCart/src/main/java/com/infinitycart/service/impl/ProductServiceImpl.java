package com.infinitycart.service.impl;

import com.infinitycart.model.Category;
import com.infinitycart.model.Product;
import com.infinitycart.model.Seller;
import com.infinitycart.repository.CategoryRepository;
import com.infinitycart.repository.ProductRepository;
import com.infinitycart.request.CreateProductRequest;
import com.infinitycart.service.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product createProduct(CreateProductRequest req, Seller seller) {
        Category category1 = categoryRepository.findByCategoryId(req.getCategory());

        if(category1 == null){
            Category category = new Category();
            category.setCategoryId(req.getCategory());
            category.setLevel(1);
            category1 = categoryRepository.save(category);
        }

        Category category2 = categoryRepository.findByCategoryId(req.getCategory2());

        if(category2 == null){
            Category category = new Category();
            category.setCategoryId(req.getCategory2());
            category.setLevel(2);
            category.setParentCategory(category1);
            category2 = categoryRepository.save(category);
        }

        Category category3 = categoryRepository.findByCategoryId(req.getCategory3());
        if(category3 == null){
            Category category = new Category();
            category.setCategoryId(req.getCategory3());
            category.setLevel(3);
            category.setParentCategory(category2);
            category3 = categoryRepository.save(category);
        }

        Product product = new Product();

        int discountPercentage = calculateDiscountPercentage(req.getMrpPrice(), req.getSellingPrice() );

        product.setSeller(seller);
        product.setCategory(category3);
        product.setDescription(req.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setSellingPrice(req.getSellingPrice());
        product.setImages(req.getImages());
        product.setMrpPrice(req.getMrpPrice());
        product.setSizes(req.getSizes());
        product.setDiscountPercent(discountPercentage);
        return productRepository.save(product);
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if(mrpPrice <= 0 || sellingPrice <= 0){
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount / mrpPrice) * 100;
        return (int) discountPercentage;
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(Long productId, Product product) throws Exception {
        findProductById(productId);
        product.setId(productId);
        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long productId) throws Exception {
        return productRepository.findById(productId).orElseThrow(() -> new Exception("Product not found")
        );
    }

    @Override
    public List<Product> searchProducts(String query) {
        productRepository.searchProduct(query);
        return List.of();
    }

    @Override
    public Page<Product> getAllProducts(
            String category,
            String brand,
            String colors,
            String sizes,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Integer pageNumber) {

        Specification<Product> specification = (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Category
            if (category != null && !category.isEmpty()) {
                Join<Product, Category> categoryJoin = root.join("category");

                predicates.add(
                        criteriaBuilder.equal(
                                categoryJoin.get("categoryId"),
                                category
                        )
                );
            }

            // Brand
            if (brand != null && !brand.isEmpty()) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("brand"),
                                brand
                        )
                );
            }

            // Colors
            if (colors != null && !colors.isEmpty()) {
                List<String> colorList = List.of(colors.split(","));

                predicates.add(
                        root.get("color").in(colorList)
                );
            }

            // Sizes
            if (sizes != null && !sizes.isEmpty()) {
                List<String> sizeList = List.of(sizes.split(","));

                // If sizes is stored as a collection
                predicates.add(
                        root.join("sizes").in(sizeList)
                );
            }

            // Minimum price
            if (minPrice != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("sellingPrice"),
                                minPrice
                        )
                );
            }

            // Maximum price
            if (maxPrice != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("sellingPrice"),
                                maxPrice
                        )
                );
            }

            // Minimum discount
            if (minDiscount != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("discountPercent"),
                                minDiscount
                        )
                );
            }

            // Stock filter
            if (stock != null && !stock.isEmpty()) {

                if (stock.equalsIgnoreCase("in_stock")) {
                    predicates.add(
                            criteriaBuilder.greaterThan(
                                    root.get("quantity"),
                                    0
                            )
                    );
                }

                if (stock.equalsIgnoreCase("out_of_stock")) {
                    predicates.add(
                            criteriaBuilder.equal(
                                    root.get("quantity"),
                                    0
                            )
                    );
                }
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };

        // Pagination
        int page = pageNumber != null ? pageNumber : 0;

        // Sorting
        org.springframework.data.domain.Pageable pageable;

        if (sort != null) {

            if (sort.equalsIgnoreCase("price_low")) {
                pageable = org.springframework.data.domain.PageRequest.of(
                        page,
                        10,
                        org.springframework.data.domain.Sort.by(
                                org.springframework.data.domain.Sort.Direction.ASC,
                                "sellingPrice"
                        )
                );

            } else if (sort.equalsIgnoreCase("price_high")) {
                pageable = org.springframework.data.domain.PageRequest.of(
                        page,
                        10,
                        org.springframework.data.domain.Sort.by(
                                org.springframework.data.domain.Sort.Direction.DESC,
                                "sellingPrice"
                        )
                );

            } else if (sort.equalsIgnoreCase("newest")) {
                pageable = org.springframework.data.domain.PageRequest.of(
                        page,
                        10,
                        org.springframework.data.domain.Sort.by(
                                org.springframework.data.domain.Sort.Direction.DESC,
                                "createdAt"
                        )
                );

            } else {
                pageable = org.springframework.data.domain.PageRequest.of(
                        page,
                        10
                );
            }

        } else {
            pageable = org.springframework.data.domain.PageRequest.of(
                    page,
                    10
            );
        }

        return productRepository.findAll(specification, pageable);
    }

    @Override
    public List<Product> getProductsBySellerId(Long sellerId) {
        return List.of((Product) productRepository.findBySellerId(sellerId));

    }
}
