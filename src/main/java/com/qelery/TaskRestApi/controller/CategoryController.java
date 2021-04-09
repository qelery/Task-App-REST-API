package com.qelery.TaskRestApi.controller;

import com.qelery.TaskRestApi.model.Category;
import com.qelery.TaskRestApi.model.Task;
import com.qelery.TaskRestApi.model.enums.Priority;
import com.qelery.TaskRestApi.model.enums.Status;
import com.qelery.TaskRestApi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public List<Category> getCategories(@RequestParam(defaultValue="100") int limit,
                                        @RequestParam(defaultValue="name,asc") String sort) {
        return categoryService.getCategories(limit, sort);
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
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        return categoryService.deleteCategory(categoryId);
    }


    // Task endpoints
    @GetMapping("/categories/tasks/all")
    public List<Task> getAllTasks(@RequestParam(required=false) Boolean overdue,
                                  @RequestParam(required=false) Priority priority,
                                  @RequestParam(required=false) Status status,
                                  @RequestParam(required=false) LocalDate dueBefore,
                                  @RequestParam(required=false) LocalDate dueAfter,
                                  @RequestParam(defaultValue="100") int limit,
                                  @RequestParam(defaultValue="dueDate,desc") String sort) {
        return categoryService.getAllTasks(overdue, priority, status, dueBefore, dueAfter, limit, sort);
    }


    @GetMapping("/categories/{categoryId}/tasks")
    public List<Task> getTasksByCategory(@PathVariable Long categoryId,
                                         @RequestParam(required=false) Boolean overdue,
                                         @RequestParam(required=false) Priority priority,
                                         @RequestParam(required=false) Status status,
                                         @RequestParam(required=false) LocalDate dueBefore,
                                         @RequestParam(required=false) LocalDate dueAfter,
                                         @RequestParam(defaultValue="100") int limit,
                                         @RequestParam(defaultValue="dueDate,desc") String sort) {
        return categoryService.getTasksByCategory(categoryId, overdue, priority, status, dueBefore, dueAfter, limit, sort);
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
    public ResponseEntity<?> markTaskComplete(@PathVariable Long categoryId,
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
    public ResponseEntity<?> deleteTask(@PathVariable Long categoryId,
                                        @PathVariable Long taskId) {
        return categoryService.deleteTask(categoryId, taskId);
    }
}