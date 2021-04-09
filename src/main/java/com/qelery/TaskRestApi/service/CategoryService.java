package com.qelery.TaskRestApi.service;


import com.qelery.TaskRestApi.exception.CategoryExistsException;
import com.qelery.TaskRestApi.exception.CategoryNotFoundException;
import com.qelery.TaskRestApi.exception.TaskNotFoundException;
import com.qelery.TaskRestApi.model.Category;
import com.qelery.TaskRestApi.model.User;
import com.qelery.TaskRestApi.model.enums.Priority;
import com.qelery.TaskRestApi.model.enums.Status;
import com.qelery.TaskRestApi.model.Task;
import com.qelery.TaskRestApi.repository.CategoryRepository;
import com.qelery.TaskRestApi.repository.TaskRepository;
import com.qelery.TaskRestApi.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, TaskRepository taskRepository) {
        this.categoryRepository = categoryRepository;
        this.taskRepository = taskRepository;
    }


    // Category CRUD methods
    public List<Category> getCategories(int limit, String sort) {
        return categoryRepository.findAllByUserId(getUser().getId(), getPageable(limit, sort));
    }

    public Category getCategory(Long categoryId) {
        Category category = categoryRepository.findByIdAndUserId(categoryId, getUser().getId());
        if (category != null) {
            return category;
        } else {
            throw new CategoryNotFoundException(categoryId);
        }
    }

    public Category createCategory(Category category) {
        if (categoryRepository.existsByNameAndUserId(category.getName(), getUser().getId())) {
            throw new CategoryExistsException(category.getName());
        } else {
            category.setUser(getUser());
            return categoryRepository.save(category);
        }
    }

    public Category updateCategory(Long categoryId, Category newCategory) {
        Category oldCategory = this.getCategory(categoryId); // handles CategoryNotFound exception
        oldCategory.setName(newCategory.getName());
        oldCategory.setDescription(newCategory.getDescription());
        return categoryRepository.save(oldCategory);
    }

    public Category partialUpdateCategory(Long categoryId, Category newCategory) {
        Category existingCategory = this.getCategory(categoryId);
        Optional.ofNullable(newCategory.getName()).ifPresent(existingCategory::setName);
        Optional.ofNullable(newCategory.getDescription()).ifPresent(existingCategory::setDescription);
        return categoryRepository.save(existingCategory);
    }

    public ResponseEntity<?> deleteCategory(Long categoryId) {
        boolean categoryExists = categoryRepository.existsByIdAndUserId(categoryId, getUser().getId());
        if (categoryExists) {
            categoryRepository.deleteById(categoryId);
            return new ResponseEntity<>("Category deleted", HttpStatus.OK);
        } else {
            throw new CategoryNotFoundException(categoryId);
        }
    }


    // Task CRUD methods
    public List<Task> getAllTasks(Boolean overdue, Priority priority, Status status,
                                  LocalDate dueBefore, LocalDate dueAfter,
                                  int limit, String sort) {
        List<Task> tasks = taskRepository.findAllByUserId(getUser().getId(), getPageable(limit, sort));
        tasks = filterTasksByParams(tasks, overdue, priority, status, dueBefore, dueAfter);
        return tasks;
    }

    public List<Task> getTasksByCategory(Long categoryId, Boolean overdue, Priority priority, Status status,
                                         LocalDate dueBefore, LocalDate dueAfter,
                                         int limit, String sort) {
        Category category = this.getCategory(categoryId); // handles CategoryNotFound exception
        List<Task> tasks = taskRepository.findByCategoryId(category.getId(), getPageable(limit, sort));
        tasks = filterTasksByParams(tasks, overdue, priority, status, dueBefore, dueAfter);
        return tasks;
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
        task.setUser(getUser());
        task.setCategory(category);
        return taskRepository.save(task);
    }

    public Task updateTask(Long categoryId, Long taskID, Task newTask) {
        Task oldTask = this.getTaskByCategory(categoryId, taskID); // handles CategoryNotFound and TaskNotFound exceptions
        oldTask.setName(newTask.getName());
        oldTask.setDescription(newTask.getDescription());
        oldTask.setDueDate(newTask.getDueDate());
        oldTask.setStatus(newTask.getStatus());
        return taskRepository.save(oldTask);
    }

    public ResponseEntity<?> markTaskComplete(Long categoryId, Long taskId) {
        Task task = this.getTaskByCategory(categoryId, taskId);
        task.setStatus(Status.COMPLETED);
        taskRepository.save(task);
        return new ResponseEntity<>("Marked task complete", HttpStatus.OK);
    }

    public Task partialUpdateTask(Long categoryId, Long taskId, Task newTask) {
        Task existingTask = this.getTaskByCategory(categoryId, taskId);
        Optional.ofNullable(newTask.getName()).ifPresent(existingTask::setName);
        Optional.ofNullable(newTask.getDescription()).ifPresent(existingTask::setDescription);
        Optional.ofNullable(newTask.getDueDate()).ifPresent(existingTask::setDueDate);
        Optional.ofNullable(newTask.getStatus()).ifPresent(existingTask::setStatus);
        return taskRepository.save(existingTask);
    }

    public ResponseEntity<?> deleteTask(Long categoryId, Long taskId) {
        Task task = this.getTaskByCategory(categoryId, taskId);
        taskRepository.delete(task);
        return new ResponseEntity<>("Removed task", HttpStatus.OK);
    }


    // Helper methods
    private List<Task> filterTasksByParams(List<Task> tasks, Boolean overdue,
                                           Priority priority, Status status,
                                           LocalDate dueBefore, LocalDate dueAfter) {
        List<Task> filteredTasks = new ArrayList<>(tasks);
        if (overdue != null) filteredTasks = filterByOverdue(filteredTasks, overdue);
        if (priority != null) filteredTasks = filterByPriority(filteredTasks, priority);
        if (status != null) filteredTasks = filterByStatus(filteredTasks, status);
        if (dueBefore != null) filteredTasks = filterByDueBefore(filteredTasks, dueBefore);
        if (dueAfter != null) filteredTasks = filterByDueAfter(filteredTasks, dueAfter);
        return filteredTasks;

    }

    private List<Task> filterByOverdue(List<Task> tasks, Boolean overdue) {
        // Tasks are considered overdue if the due date is in the past and the status is still pending
        LocalDate today = LocalDate.now();
        if (overdue) {
            return tasks.stream()
                    .filter(task -> today.isAfter(task.getDueDate())
                            && task.getStatus() == Status.PENDING)
                    .collect(Collectors.toList());
        } else {
            return tasks.stream()
                    .filter(task -> !today.isAfter(task.getDueDate())
                            || task.getStatus() == Status.COMPLETED)
                    .collect(Collectors.toList());
        }
    }

    private List<Task> filterByPriority(List<Task> tasks, Priority priority) {
        return tasks.stream()
                .filter(task -> task.getPriority().equals(priority))
                .collect(Collectors.toList());
    }

    private List<Task> filterByStatus(List<Task> tasks, Status status) {
        return tasks.stream()
                .filter(task -> task.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    private List<Task> filterByDueBefore(List<Task> tasks, LocalDate dueBefore) {
        return tasks.stream()
                .filter(task -> task.getDueDate().isBefore(dueBefore))
                .collect(Collectors.toList());
    }

    private List<Task> filterByDueAfter(List<Task> tasks, LocalDate dueAfter) {
        return tasks.stream()
                .filter(task -> task.getDueDate().isAfter(dueAfter))
                .collect(Collectors.toList());
    }

    private Pageable getPageable(int limit, String sort) {
        String[] sortParams = sort.split(",");
        String property = sortParams[0];
        Sort.Direction direction = sortParams[1].equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(0, limit, Sort.by(direction, property));
    }

    private User getUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().
                getAuthentication()
                .getPrincipal();
        return userDetails.getUser();
    }
}