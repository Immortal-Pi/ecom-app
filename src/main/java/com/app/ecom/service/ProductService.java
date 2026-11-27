package com.app.ecom.service;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.models.Product;
import com.app.ecom.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.mapper.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        updateProductFromRequest(product,productRequest);
        Product savedProduct=productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }

    private ProductResponse mapToProductResponse(Product savedProduct) {
        ProductResponse response = new ProductResponse();
        response.setId(String.valueOf(savedProduct.getId()));
        response.setActive(savedProduct.getActive());
        response.setCategory(savedProduct.getCategory());
        response.setDescription(savedProduct.getDescription());
        response.setName(savedProduct.getName());
        response.setImageUrl(savedProduct.getImageUrl());
        response.setPrice(savedProduct.getPrice());
        response.setStockQuantity(savedProduct.getStockQuantity());
        return  response;
    }

    private void updateProductFromRequest(Product product, ProductRequest productRequest) {
        product.setCategory(productRequest.getCategory());
        product.setDescription(productRequest.getDescription());
        product.setName(productRequest.getName());
        product.setImageUrl(productRequest.getImageUrl());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setActive(productRequest.getActive());
    }


    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id)
                .map(existingProduct->{
                    updateProductFromRequest(existingProduct,productRequest);
                    Product savedProduct=productRepository.save(existingProduct);
                    return mapToProductResponse(savedProduct);
                });
    }

    public Optional<ProductResponse> fetchProduct(Long id) {
        return productRepository.findById(id)
                .map(this::mapToProductResponse);
    }

    public List<ProductResponse> fetchallProducts(){

//        return productRepository.findAll().
//                stream()
//                .map(this::mapToProductResponse)
//                .collect(Collectors.toList());
        return productRepository.findByActiveTrue().
                stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public boolean deleteProduct(Long id) {
         return productRepository.findById(id)
                 .map(product -> {
                     product.setActive(false);
                     productRepository.save(product);
                     return true;
                 })
                 .orElse(false);


    }
}
