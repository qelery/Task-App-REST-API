package com.qelery.TodoRestApi.controller;

import com.qelery.TodoRestApi.model.Category;
import com.qelery.TodoRestApi.model.Task;
import com.qelery.TodoRestApi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/api")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Category endpoints
    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryService.getCategories();
    }

    @GetMapping("/categories/{categoryId}")
    public Category getCategory(@PathVariable Long categoryId) {
        return categoryService.getCategory(categoryId);
    }

    @PostMapping("/categories")
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @PutMapping("/categories/{categoryId}")
    public Category updateCategory(@PathVariable Long categoryId, @RequestBody Category category) {
        return categoryService.updateCategory(categoryId, category);
    }

    @DeleteMapping("/categories/{categoryId}")
    public Category deleteCategory(@PathVariable Long categoryId) {
        return categoryService.deleteCategory(categoryId);
    }

    // Task endpoints
    @GetMapping("/categories/tasks/all")
    public List<Task> getAllTasks(@RequestParam(required=false) Boolean completed) {
        return categoryService.getAllTasks(completed);
    }

    @GetMapping("/categories/{categoryId}/tasks")
    public List<Task> getTasksByCategory(@PathVariable Long categoryId,
                                         @RequestParam(required=false) Boolean completed) {
        return categoryService.getTasksByCategory(categoryId, completed);
    }

    @GetMapping("/categories/{categoryId}/tasks/{taskId}")
    public Task getTaskByCategory(@PathVariable Long categoryId,
                                  @PathVariable Long taskId) {
        return categoryService.getTaskByCategory(categoryId, taskId);
    }

    @PostMapping("/categories/{categoryId}/tasks")
    public Task createTask(@PathVariable Long categoryId,
                           @RequestBody Task task) {
        return categoryService.createTask(categoryId, task);
    }

    @PutMapping("/categories/{categoryId}/tasks/{taskId}")
    public Task updateTask(@PathVariable Long categoryId,
                           @PathVariable Long taskId,
                           @RequestBody Task task) {
        return categoryService.updateTask(categoryId, taskId, task);
    }

    @DeleteMapping("/categories/{categoryId}/tasks/{taskId}")
    public Task deleteTask(@PathVariable Long categoryId,
                           @PathVariable Long taskId) {
        return categoryService.deleteTask(categoryId, taskId);
    }
}