package com.qelery.TodoRestApi.service;


import com.qelery.TodoRestApi.exception.CategoryExistsException;
import com.qelery.TodoRestApi.exception.CategoryNotFoundException;
import com.qelery.TodoRestApi.exception.TaskNotFoundException;
import com.qelery.TodoRestApi.model.Category;
import com.qelery.TodoRestApi.model.Task;
import com.qelery.TodoRestApi.repository.CategoryRepository;
import com.qelery.TodoRestApi.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;
    private TaskRepository taskRepository;

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Autowired
    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    // Category CRUD methods
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


    // Task CRUD methods
    public List<Task> getAllTasks(Boolean completedRequestParam) {
        List<Category> allCategories = categoryRepository.findAll();
        List<Task> allTasks = allCategories.stream()
                .map(Category::getTasks)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        if (completedRequestParam == null) return allTasks;
        return filterByCompletionStatus(allTasks, completedRequestParam);
    }

    public List<Task> getTasksByCategory(Long categoryId, Boolean completedRequestParam) {
        Category category = this.getCategory(categoryId);
        List<Task> tasks = category.getTasks();
        if (completedRequestParam == null) return tasks;
        return filterByCompletionStatus(tasks, completedRequestParam);
    }

    public Task getTaskByCategory(Long categoryId, Long taskId) {
        Category category = this.getCategory(categoryId);
        Optional<Task> optionalTask = category.getTasks()
                .stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst();
        return optionalTask.orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    public Task createTask(Long categoryId, Task task) {
        Category category = this.getCategory(categoryId);
        task.setCategory(category);
        return taskRepository.save(task);
    }

    public Task updateTask(Long categoryId, Long taskID, Task newTask) {
        Task oldTask = this.getTaskByCategory(categoryId, taskID);
        oldTask.setName(newTask.getName());
        oldTask.setDescription(newTask.getDescription());
        oldTask.setDueDate(newTask.getDueDate());
        oldTask.setIsDone(newTask.getIsDone());
        return taskRepository.save(oldTask);
    }

    public Task deleteTask(Long categoryId, Long taskId) {
        Task task = this.getTaskByCategory(categoryId, taskId);
        taskRepository.delete(task);
        return task;
    }

    private List<Task> filterByCompletionStatus(List<Task> tasks, boolean completionStatus) {
        if (completionStatus) {
            return tasks.stream()
                    .filter(Task::getIsDone)
                    .collect(Collectors.toList());
        } else {
            return tasks.stream()
                    .filter(task -> !task.getIsDone())
                    .collect(Collectors.toList());
        }
    }
}