package com.example.fashionshop.dto.response.products;

import lombok.Getter;
import lombok.Setter;
//
@Getter
@Setter
public class ProductVariantResponse {
    private Long id;
    private Integer stockQuantity;
    private ProductsResponse productResponse;
    private SizeResponse size;
}
