package com.example.fashionshop.service;


import com.example.fashionshop.dto.request.auth.LoginRequest;
import com.example.fashionshop.dto.request.auth.RegisterRequest;
import com.example.fashionshop.model.Address;
import com.example.fashionshop.model.User;
import com.example.fashionshop.common.Role;
import com.example.fashionshop.common.Status;
import com.example.fashionshop.repository.AddressRepository;
import com.example.fashionshop.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    // đăng kí
    @Transactional
    public boolean register (RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()) != null) {
            return false;
        }
        User user = new User();

        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setEmail(registerRequest.getEmail());
        user.setFullName(registerRequest.getFullName());
        user.setPhone(registerRequest.getPhone());
        user.setRole(Role.CUSTOMER);
        user.setStatus(Status.ACTIVE);

        Address address = new Address();
        address.setAddressLine(registerRequest.getAddressLine());
        address.setCity(registerRequest.getCity());
        address.setWard(registerRequest.getWard());
        address.setDistrict(registerRequest.getDistrict());

        address.setUser(user);
        user.setAddress(address);
        userRepository.save(user);
        addressRepository.save(address);


        return true;

    }

    // đăng nhập
    public boolean login (LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null) {
            return false;
        }
        if (user.getPassword().equals(loginRequest.getPassword())) {
            return true;
        }
        return false;
    }

}
