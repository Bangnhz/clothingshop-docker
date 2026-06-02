package com.example.fashionshop.service;


import com.example.fashionshop.dto.response.products.ProductImageResponse;
import com.example.fashionshop.dto.response.products.ProductVariantResponse;
import com.example.fashionshop.dto.response.products.ProductsResponse;
import com.example.fashionshop.dto.response.products.SizeResponse;
import com.example.fashionshop.model.*;
import com.example.fashionshop.repository.CategoryRepository;
import com.example.fashionshop.repository.ProductRepository;
import com.example.fashionshop.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SizeRepository sizeRepository;
    // hiển thi toàn bộ sản phẩm
    public List<ProductsResponse> getAllProducts() {

        List<Product> products = productRepository.findAll();
        List<ProductsResponse> productsResponses = new ArrayList<>();

        for (Product product : products) {
            ProductsResponse request = new ProductsResponse();

            request.setId(product.getId());
            request.setName(product.getName());
            request.setPrice(product.getPrice());
            request.setDescription(product.getDescription());
            request.setPrice(product.getPrice());


            List<ProductImageResponse> productImageResponses = new ArrayList<>();
            for (ProductImage image : product.getImages()) {
                ProductImageResponse productImageResponse = new ProductImageResponse();
                productImageResponse.setImageUrl(image.getImageUrl());
                productImageResponses.add(productImageResponse);

            }
            List<ProductVariantResponse> productVariantResponses = new ArrayList<>();
            for (ProductVariant variant : product.getVariants()) {
                ProductVariantResponse productVariantResponse = new ProductVariantResponse();
                productVariantResponse.setId(variant.getId());
                Size size = variant.getSize();
                SizeResponse sizeResponse = new SizeResponse();
                sizeResponse.setName(size.getName());

                productVariantResponse.setSize(sizeResponse);
                productVariantResponse.setStockQuantity(variant.getStockQuantity());
                productVariantResponses.add(productVariantResponse);
            }

            request.setProductVariants(productVariantResponses);
            request.setImages(productImageResponses);
            productsResponses.add(request);
        }
        return productsResponses;
    }

    // hiển thị chi tiết san pham
    public ProductsResponse getProductDetail(long id) {
        Product product = productRepository.findById((int)id).orElse(null);
        if (product == null) return null;
        ProductsResponse request = new ProductsResponse();

        request.setId(product.getId());
        request.setName(product.getName());
        request.setPrice(product.getPrice());
        request.setDescription(product.getDescription());

        List<ProductImageResponse> productImageResponses = new ArrayList<>();
        for (ProductImage image : product.getImages()) {
            ProductImageResponse productImageResponse = new ProductImageResponse();
            productImageResponse.setImageUrl(image.getImageUrl());
            productImageResponses.add(productImageResponse);
        }

        List<ProductVariantResponse> productVariantResponses = new ArrayList<>();
        for (ProductVariant variant : product.getVariants()) {
            ProductVariantResponse productVariantResponse = new ProductVariantResponse();
            productVariantResponse.setId(variant.getId());
            if (variant.getSize() != null) {
                SizeResponse sizeResponse = new SizeResponse();
                sizeResponse.setName(variant.getSize().getName());
                productVariantResponse.setSize(sizeResponse);
            }
            productVariantResponse.setStockQuantity(variant.getStockQuantity());
            productVariantResponses.add(productVariantResponse);
        }

        request.setProductVariants(productVariantResponses);
        request.setImages(productImageResponses);

        return request;
    }

    // loc san pham theo muc gia
    public List<ProductsResponse> filterPrice(double minPrice, double maxPrice) {
        System.out.println(minPrice);
        System.out.println(maxPrice);
        List<Product> products = productRepository.findByPriceBetweenOrderByPriceAsc(minPrice, maxPrice);
        List<ProductsResponse> productsResponses = new ArrayList<>();

        for (Product product : products) {
            ProductsResponse request = new ProductsResponse();

            request.setId(product.getId());
            request.setName(product.getName());
            request.setPrice(product.getPrice());
            request.setDescription(product.getDescription());
            request.setPrice(product.getPrice());


            List<ProductImageResponse> productImageResponses = new ArrayList<>();
            for (ProductImage image : product.getImages()) {
                ProductImageResponse productImageResponse = new ProductImageResponse();
                productImageResponse.setImageUrl(image.getImageUrl());
                productImageResponses.add(productImageResponse);

            }
            List<ProductVariantResponse> productVariantResponses = new ArrayList<>();
            for (ProductVariant variant : product.getVariants()) {
                ProductVariantResponse productVariantResponse = new ProductVariantResponse();
                productVariantResponse.setId(variant.getId());
                Size size = variant.getSize();
                SizeResponse sizeResponse = new SizeResponse();
                sizeResponse.setName(size.getName());

                productVariantResponse.setSize(sizeResponse);
                productVariantResponse.setStockQuantity(variant.getStockQuantity());
                productVariantResponses.add(productVariantResponse);
            }

            request.setProductVariants(productVariantResponses);
            request.setImages(productImageResponses);
            productsResponses.add(request);
        }

        return productsResponses;
    }

    // tim kiem san pham theo ten
    public List<ProductsResponse> filterName(String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        List<ProductsResponse> productsResponses = new ArrayList<>();

        for (Product product : products) {
            ProductsResponse request = new ProductsResponse();

            request.setId(product.getId());
            request.setName(product.getName());
            request.setPrice(product.getPrice());
            request.setDescription(product.getDescription());
            request.setPrice(product.getPrice());


            List<ProductImageResponse> productImageResponses = new ArrayList<>();
            for (ProductImage image : product.getImages()) {
                ProductImageResponse productImageResponse = new ProductImageResponse();
                productImageResponse.setImageUrl(image.getImageUrl());
                productImageResponses.add(productImageResponse);

            }
            List<ProductVariantResponse> productVariantResponses = new ArrayList<>();
            for (ProductVariant variant : product.getVariants()) {
                ProductVariantResponse productVariantResponse = new ProductVariantResponse();
                productVariantResponse.setId(variant.getId());
                Size size = variant.getSize();
                SizeResponse sizeResponse = new SizeResponse();
                sizeResponse.setName(size.getName());

                productVariantResponse.setSize(sizeResponse);
                productVariantResponse.setStockQuantity(variant.getStockQuantity());
                productVariantResponses.add(productVariantResponse);
            }

            request.setProductVariants(productVariantResponses);
            request.setImages(productImageResponses);
            productsResponses.add(request);
        }
        return productsResponses;

    }
    // them san pham
    public boolean addProduct(ProductsResponse productsResponse) {
        Product product = new Product();
        product.setId((int)productsResponse.getId()); // Manual ID from user
        product.setName(productsResponse.getName());
        product.setDescription(productsResponse.getDescription());
        product.setPrice(productsResponse.getPrice());
        product.setCreatedAt(LocalDateTime.now());

        Category category = categoryRepository.findById(productsResponse.getCategoryId()).orElse(null);
        product.setCategory(category);

        try
        {
            // Initial save to establish ID if manual (though IDENTITY was removed, manual ID needs save)
            // But we need to save images and variants together usually.
            
            if (productsResponse.getImages() != null) {
                List<ProductImage> images = new ArrayList<>();
                for (ProductImageResponse imgRes : productsResponse.getImages()) {
                    ProductImage pi = new ProductImage();
                    pi.setImageUrl(imgRes.getImageUrl());
                    pi.setIsMain(images.isEmpty());
                    pi.setProduct(product);
                    images.add(pi);
                }
                product.setImages(images);
            }

            if (productsResponse.getProductVariants() != null) {
                List<ProductVariant> variants = new ArrayList<>();
                for (ProductVariantResponse varRes : productsResponse.getProductVariants()) {
                    ProductVariant pv = new ProductVariant();
                    pv.setStockQuantity(varRes.getStockQuantity());
                    pv.setProduct(product);
                    
                    if (varRes.getSize() != null) {
                        Size s = null;
                        if (varRes.getSize().getId() != null) {
                            s = sizeRepository.findById(varRes.getSize().getId()).orElse(null);
                        } else if (varRes.getSize().getName() != null) {
                            s = sizeRepository.findByName(varRes.getSize().getName()).orElse(null);
                        }
                        pv.setSize(s);
                    }
                    variants.add(pv);
                }
                product.setVariants(variants);
            }

            productRepository.save(product);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

    }

    // sua san pham
    public boolean updateProduct(Integer id , ProductsResponse productsResponse) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return false;

        product.setName(productsResponse.getName());
        product.setDescription(productsResponse.getDescription());
        product.setPrice(productsResponse.getPrice());
        
        Category category = categoryRepository.findById(productsResponse.getCategoryId()).orElse(null);
        product.setCategory(category);

        try
        {
            // Sync Images
            if (productsResponse.getImages() != null) {
                product.getImages().clear();
                for (ProductImageResponse imgRes : productsResponse.getImages()) {
                    ProductImage pi = new ProductImage();
                    pi.setImageUrl(imgRes.getImageUrl());
                    pi.setIsMain(product.getImages().isEmpty());
                    pi.setProduct(product);
                    product.getImages().add(pi);
                }
            }

            // Sync Variants
            if (productsResponse.getProductVariants() != null) {
                product.getVariants().clear();
                for (ProductVariantResponse varRes : productsResponse.getProductVariants()) {
                    ProductVariant pv = new ProductVariant();
                    pv.setStockQuantity(varRes.getStockQuantity());
                    pv.setProduct(product);
                    
                    if (varRes.getSize() != null) {
                        Size s = null;
                        if (varRes.getSize().getId() != null) {
                            s = sizeRepository.findById(varRes.getSize().getId()).orElse(null);
                        } else if (varRes.getSize().getName() != null) {
                            s = sizeRepository.findByName(varRes.getSize().getName()).orElse(null);
                        }
                        pv.setSize(s);
                    }
                    product.getVariants().add(pv);
                }
            }

            productRepository.save(product);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    // xoa san pham
    public boolean deleteProduct(Integer id) {

        try
        {
            productRepository.deleteById(id);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
