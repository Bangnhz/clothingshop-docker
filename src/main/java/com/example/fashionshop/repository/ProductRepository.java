package com.example.fashionshop.repository;

import com.example.fashionshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
//
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByPriceBetweenOrderByPriceAsc(double minPrice, double maxPrice);
    List<Product> findByNameContainingIgnoreCase(String name);
}
