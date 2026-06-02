package com.example.fashionshop.service;

import com.example.fashionshop.dto.CartDTO;
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
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    // hien thi ds sp trong gio hang
    public CartDTO getCartByUsername(String username) {

        User user = userRepository.findByUsername(username);
        if (user == null) return new CartDTO();

        Cart cart = cartRepository.findByUserId(user.getId());

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        return convertToDTO(cart);
    }

    private CartDTO convertToDTO(Cart cart) {

        CartDTO cartDTO = new CartDTO();
        List<CartItemDTO> cartItemDTOs = new ArrayList<>();

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        for (CartItem item : cartItems) {

            CartItemDTO dto = new CartItemDTO();
            dto.setQuantity(item.getQuantity());

            ProductVariant variant = item.getProductVariant();
            if (variant == null) continue;

            ProductVariantResponse variantRes = new ProductVariantResponse();

            Product product = variant.getProduct();
            if (product != null) {

                ProductsResponse productRes = new ProductsResponse();
                productRes.setName(product.getName());
                productRes.setPrice(product.getPrice());

                List<ProductImageResponse> images = new ArrayList<>();
                for (ProductImage img : product.getImages()) {
                    ProductImageResponse imgRes = new ProductImageResponse();
                    imgRes.setImageUrl(img.getImageUrl());
                    images.add(imgRes);
                }

                productRes.setImages(images);
                productRes.setId(product.getId()); // Set product ID
                variantRes.setProductResponse(productRes);
            }

            variantRes.setId(variant.getId()); // Set variant ID
            Size size = variant.getSize();
            if (size != null) {
                SizeResponse sizeRes = new SizeResponse();
                sizeRes.setId(size.getId());
                sizeRes.setName(size.getName());
                variantRes.setSize(sizeRes);
            }

            dto.setId(item.getId()); // Set CartItem ID
            dto.setProductVariantResponse(variantRes);
            cartItemDTOs.add(dto);
        }

        cartDTO.setItems(cartItemDTOs);
        return cartDTO;
    }
}