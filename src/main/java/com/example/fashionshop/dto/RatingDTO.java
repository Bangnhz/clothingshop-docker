package com.example.fashionshop.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RatingDTO {
    private Integer id;
    private Integer star;
    private String comment;
    private LocalDateTime created_at;
    private Integer product_id;
}
