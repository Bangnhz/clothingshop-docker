package com.example.fashionshop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String basePath = System.getProperty("user.dir") + "/BTL_LTW_4/frontend/";
        
        registry.addResourceHandler("/css/**")
                .addResourceLocations("file:" + basePath + "css/");

        registry.addResourceHandler("/js/**")
                .addResourceLocations("file:" + basePath + "js/");

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + basePath + "images/");

        registry.addResourceHandler("/admin/**")
                .addResourceLocations("file:" + basePath + "admin/");

        registry.addResourceHandler("/**")
                .addResourceLocations("file:" + basePath);
    }
}
