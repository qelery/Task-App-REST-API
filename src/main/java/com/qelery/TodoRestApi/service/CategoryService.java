package com.qelery.TodoRestApi.service;


import com.qelery.TodoRestApi.exception.CategoryExistsException;
import com.qelery.TodoRestApi.exception.CategoryNotFoundException;
import com.qelery.TodoRestApi.model.Category;
import com.qelery.TodoRestApi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() ->  new CategoryNotFoundException(categoryId));
    }

    public Category createCategory(Category category) {
        String searchedName = category.getName();
        if (categoryRepository.existsByName(searchedName)) {
            throw new CategoryExistsException(searchedName);
        } else {
            return categoryRepository.save(category);
        }
    }

    public Category updateCategory(Long categoryId, Category newCategory) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isPresent()) {
            Category oldCategory = optionalCategory.get();
            oldCategory.setName(newCategory.getName());
            oldCategory.setDescription(newCategory.getDescription());
            return categoryRepository.save(oldCategory);
        } else {
            throw new CategoryNotFoundException(categoryId);
        }
    }

    public Category deleteCategory(Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isPresent()) {
            categoryRepository.deleteById(categoryId);
            return optionalCategory.get();
        } else {
            throw new CategoryNotFoundException(categoryId);
        }
    }
}