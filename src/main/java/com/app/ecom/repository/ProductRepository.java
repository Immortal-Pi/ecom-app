package com.app.ecom.repository;

import com.app.ecom.dto.ProductResponse;
import com.app.ecom.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByActiveTrue();
}
