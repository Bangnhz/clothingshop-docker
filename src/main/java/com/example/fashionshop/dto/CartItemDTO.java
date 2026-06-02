package com.example.fashionshop.dto;

import com.example.fashionshop.dto.response.products.ProductVariantResponse;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CartItemDTO {
    private Integer id;

    private Integer quantity;

    private ProductVariantResponse productVariantResponse;
}
