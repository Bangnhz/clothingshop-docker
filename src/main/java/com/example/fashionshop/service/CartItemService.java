package com.example.fashionshop.service;

import com.example.fashionshop.dto.CartItemDTO;
import com.example.fashionshop.dto.response.products.ProductImageResponse;
import com.example.fashionshop.dto.response.products.ProductVariantResponse;
import com.example.fashionshop.dto.response.products.ProductsResponse;
import com.example.fashionshop.dto.response.products.SizeResponse;
import com.example.fashionshop.model.*;
import com.example.fashionshop.repository.CartItemRepository;
import com.example.fashionshop.repository.CartRepository;
import com.example.fashionshop.repository.ProductRepository;
import com.example.fashionshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    // chi tiet sp
    public CartItemDTO getCartItemById(Long id) {
        CartItem cartItem = cartItemRepository.findById(id).orElse(null);
        CartItemDTO cartItemDTO = new CartItemDTO();

        cartItemDTO.setQuantity(cartItem.getQuantity());
        ProductVariant productVariant = cartItem.getProductVariant();
        ProductVariantResponse productVariantResponse = new ProductVariantResponse();

        Size size  = productVariant.getSize();
        SizeResponse sizeResponse = new SizeResponse();
        sizeResponse.setId(size.getId());
        sizeResponse.setName(size.getName());
        Product product = productVariant.getProduct();
        List<ProductImage> productImageList = product.getImages();
        List<ProductImageResponse> productImageResponseList = new ArrayList<>();
        for(ProductImage productImage : productImageList){
            ProductImageResponse productImageResponse = new ProductImageResponse();
            productImageResponse.setImageUrl(productImage.getImageUrl());
            productImageResponseList.add(productImageResponse);
        }

        ProductsResponse productsResponse = new ProductsResponse();
        productsResponse.setId(product.getId());
        productsResponse.setName(product.getName());

        productsResponse.setPrice(product.getPrice());
        productsResponse.setDescription(product.getDescription());
        productsResponse.setImages(productImageResponseList);
        
        productVariantResponse.setId(productVariant.getId());
        productVariantResponse.setProductResponse(productsResponse);
        productVariantResponse.setSize(sizeResponse);

        cartItemDTO.setId(cartItem.getId());

        cartItemDTO.setProductVariantResponse(productVariantResponse);

        return cartItemDTO;
    }

    // them sp vao cart
    public void addToCart(String username, Long productId, Integer quantity, Long variantId) {

        if (username == null) {
            throw new RuntimeException("Guest không dùng DB cart");
        }

        User user = userRepository.findByUsername(username);
        if (user == null) throw new RuntimeException("User not found");

        Cart cart = cartRepository.findByUserId(user.getId());

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        Product product = productRepository.findById(productId.intValue()).orElseThrow();

        ProductVariant variant = product.getVariants()
                .stream()
                .filter(v -> v.getId() == variantId.longValue())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Variant not found: " + variantId));

        CartItem existing = cartItemRepository
                .findByCartIdAndProductVariantId(cart.getId(), variantId);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            cartItemRepository.save(existing);
            return;
        }

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProductVariant(variant);
        item.setQuantity(quantity);

        cartItemRepository.save(item);
    }

    // xoa sp
    public boolean deleteCartItem(long id) {

        try {
            cartItemRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}