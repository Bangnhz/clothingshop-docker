package com.example.fashionshop.repository;

import com.example.fashionshop.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
//
@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
}
