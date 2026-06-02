package com.example.fashionshop.controller;

import com.example.fashionshop.dto.response.ResponseData;
import com.example.fashionshop.service.CartItemService;
import com.example.fashionshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/cart/items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CartService cartService;

    // them san pham vao cart (user)
    @PostMapping
    public ResponseEntity<ResponseData> addToCart(
            Authentication authentication,
            @RequestParam Long productId,
            @RequestParam Long variantId,
            @RequestParam Integer quantity) {

        ResponseData res = new ResponseData();

        if (authentication == null) {
            res.setData("Guest dùng localStorage, không gọi API này");
            return ResponseEntity.ok(res);
        }

        String username = authentication.getName();

        cartItemService.addToCart(username, productId, quantity, variantId);

        res.setData("Add to cart success");
        return ResponseEntity.ok(res);
    }

    // xoa sp trong cart (user)
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> delete(@PathVariable long id) {

        ResponseData res = new ResponseData();
        res.setData(cartItemService.deleteCartItem(id));

        return ResponseEntity.ok(res);
    }

    // xem chi tiet sp trong cart
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getCartItem(@PathVariable Long id) {

        ResponseData responseData = new ResponseData();
        responseData.setData(cartItemService.getCartItemById(id));

        return ResponseEntity.ok(responseData);
    }
}