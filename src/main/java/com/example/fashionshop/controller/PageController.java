package com.example.fashionshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/", "/home"})
    public String index() {
        return "forward:/index.html";
    }

    @GetMapping("/about")
    public String about() {
        return "forward:/about.html";
    }

    @GetMapping("/cart")
    public String cart() {
        return "forward:/cart.html";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "forward:/checkout.html";
    }

    @GetMapping("/login")
    public String login() {
        return "forward:/login.html";
    }

    @GetMapping("/orders")
    public String orders() {
        return "forward:/orders.html";
    }

    @GetMapping("/product")
    public String product() {
        return "forward:/product.html";
    }

    @GetMapping("/products")
    public String products() {
        return "forward:/products.html";
    }

    @GetMapping("/profile")
    public String profile() {
        return "forward:/profile.html";
    }

    @GetMapping("/register")
    public String register() {
        return "forward:/register.html";
    }

    @GetMapping("/track-order")
    public String trackOrder() {
        return "forward:/track-order.html";
    }

    @GetMapping("/admin")
    public String admin() {
        return "forward:/admin/index.html";
    }
}
