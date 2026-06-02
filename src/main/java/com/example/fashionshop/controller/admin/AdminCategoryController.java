package com.example.fashionshop.controller.admin;


import com.example.fashionshop.dto.CategoryDTO;
import com.example.fashionshop.dto.response.ResponseData;
import com.example.fashionshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminCategoryController {
    @Autowired
    private CategoryService categoryService;

    // them category
    @PostMapping("/category")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDTO categoryDTO) {

        ResponseData responseData = new ResponseData();
        responseData.setSuccess(categoryService.addCategory(categoryDTO));
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    // xoa category
    @DeleteMapping("/category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable long id) {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(categoryService.deleteCategory(id));
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
    // sua category
    @PutMapping("/category/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable int id,
                                            @RequestBody CategoryDTO categoryDTO) {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(categoryService.updateCategory(id, categoryDTO));
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

}
