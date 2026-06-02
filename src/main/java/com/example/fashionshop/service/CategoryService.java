package com.example.fashionshop.service;


import com.example.fashionshop.dto.CategoryDTO;
import com.example.fashionshop.model.Category;
import com.example.fashionshop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    // them category
    public boolean addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        try
        {
            categoryRepository.save(category);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    // xoa category
    public boolean deleteCategory(long id) {
        try
        {
            categoryRepository.deleteById(id);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    // sua category
    public boolean updateCategory(int id , CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(id);
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        try
        {
            categoryRepository.save(category);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }


    }
}
