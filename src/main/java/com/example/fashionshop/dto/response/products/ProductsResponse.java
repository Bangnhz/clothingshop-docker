package com.example.fashionshop.dto.response.products;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductsResponse {
    private long id;
    private String name;
    private String description;
    private Double price;
    private List<ProductImageResponse> images;
    private List<ProductVariantResponse> productVariants;
    private long categoryId;
}
