package com.example.fashionshop.controller;


import com.example.fashionshop.dto.request.auth.LoginRequest;
import com.example.fashionshop.dto.request.auth.RegisterRequest;
import com.example.fashionshop.dto.response.ResponseData;
import com.example.fashionshop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    // đăng kí
    @PostMapping("/register")
    public ResponseEntity<?> register (@Valid @RequestBody RegisterRequest registerRequest)
    {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(authService.register(registerRequest));
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    // đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody LoginRequest loginRequest)
    {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(authService.login(loginRequest));
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

}
