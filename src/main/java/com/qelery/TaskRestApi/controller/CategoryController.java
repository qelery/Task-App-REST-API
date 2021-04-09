package com.qelery.TaskRestApi.controller;

import com.qelery.TaskRestApi.model.Category;
import com.qelery.TaskRestApi.model.Task;
import com.qelery.TaskRestApi.service.CategoryService;
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

    @PatchMapping("/categories/{categoryId}")
    public Category partialUpdateCategory(@PathVariable Long categoryId,
                                          @RequestBody Category category) {
        return categoryService.partialUpdateCategory(categoryId, category);
    }

    @DeleteMapping("/categories/{categoryId}")
    public Category deleteCategory(@PathVariable Long categoryId) {
        return categoryService.deleteCategory(categoryId);
    }


    // Task endpoints
    @GetMapping("/categories/tasks/all")
    public List<Task> getAllTasks(@RequestParam(required=false) Boolean completed,
                                  @RequestParam(required=false) Boolean overdue) {
        return categoryService.getAllTasks(completed, overdue);
    }

    @GetMapping("/categories/{categoryId}/tasks")
    public List<Task> getTasksByCategory(@PathVariable Long categoryId,
                                         @RequestParam(required=false) Boolean completed,
                                         @RequestParam(required=false) Boolean overdue) {
        return categoryService.getTasksByCategory(categoryId, completed, overdue);
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

    @PutMapping("/categories/{categoryId}/tasks/{taskId}/complete")
    public Task markTaskComplete(@PathVariable Long categoryId,
                                 @PathVariable Long taskId) {
        return categoryService.markTaskComplete(categoryId, taskId);
    }

    @PatchMapping("/categories/{categoryId}/tasks/{taskId}")
    public Task partialUpdateTask(@PathVariable Long categoryId,
                                  @PathVariable Long taskId,
                                  @RequestBody Task task) {
        return categoryService.partialUpdateTask(categoryId, taskId, task);
    }

    @DeleteMapping("/categories/{categoryId}/tasks/{taskId}")
    public Task deleteTask(@PathVariable Long categoryId,
                           @PathVariable Long taskId) {
        return categoryService.deleteTask(categoryId, taskId);
    }
}