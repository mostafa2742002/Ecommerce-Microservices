package com.micro.productservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micro.productservice.dto.CategoryDTO;
import com.micro.productservice.entity.Category;
import com.micro.productservice.exceptions.ResourceNotFoundException;
import com.micro.productservice.repo.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(CategoryDTO category) {
        Category newCategory = new Category(category);
        return categoryRepository.save(newCategory);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", id));
    }

    public Category updateCategory(Integer id, CategoryDTO updatedCategory) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category", "Category Id", id);
        }

        Category category = new Category(updatedCategory);
        category.setId(id);

        return categoryRepository.save(category);
    }

    public void deleteCategory(Integer id) {

        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category", "Category Id", id);
        }

        categoryRepository.deleteById(id);
    }
}
