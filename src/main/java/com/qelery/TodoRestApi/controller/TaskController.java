package com.qelery.TodoRestApi.controller;

import com.qelery.TodoRestApi.exception.InformationExistsException;
import com.qelery.TodoRestApi.exception.InformationNotFoundException;
import com.qelery.TodoRestApi.model.Category;
import com.qelery.TodoRestApi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="/api")
public class TaskController {

    private CategoryRepository categoryRepository;

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/categories/{categoryId}")
    public Category getCategory(@PathVariable Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        return category.orElseThrow(() -> new InformationNotFoundException("Category with id " + categoryId + " not be found"));
//        if (category.isPresent()) {
//            return category;
//        } else {
//            throw new InformationNotFoundException("Category with id " + categoryId + " not be found");
//        }
    }

    @PostMapping("/categories")
    public Category createCategory(@RequestBody Category categoryObject) {
        Category category = categoryRepository.findByName(categoryObject.getName());
        if (category != null) {
            throw new InformationExistsException("category with name " + category.getName() + " already exists");
        } else {
            return categoryRepository.save(categoryObject);
        }
    }

    @PutMapping("/categories/{categoryId}")
    public Category updateCategory(@PathVariable Long categoryId, @RequestBody Category categoryObject) {

        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isPresent()) {
            if (categoryObject.getName().equals(optionalCategory.get().getName())) {
                throw new InformationExistsException("Category ");
            } else {
                Category updateCategory = optionalCategory.get();
                updateCategory.setName(categoryObject.getName());
                updateCategory.setDescription(categoryObject.getDescription());
                return categoryRepository.save(updateCategory);
            }
        } else {
            throw new InformationNotFoundException("Category with id " + categoryId + " not be found");
        }
    }

    @DeleteMapping("/categories/{categoryId}")
    public Optional<Category> deleteCategory(@PathVariable Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isPresent()) {
            categoryRepository.deleteById(categoryId);
            return optionalCategory;
        } else {
            throw new InformationNotFoundException("Category with id " + categoryId + " not be found");
        }
    }
}