package com.example.fashionshop.dto;

import com.example.fashionshop.dto.response.products.ProductVariantResponse;
import lombok.Getter;
import lombok.Setter;
//
@Getter
@Setter
public class OrderItemDTO {
    private Integer id;
    private Double price;
    private Integer quantity;
    private ProductVariantResponse variant;

}
