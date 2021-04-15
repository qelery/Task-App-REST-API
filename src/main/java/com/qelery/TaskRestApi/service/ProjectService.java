package com.qelery.TaskRestApi.service;


import com.qelery.TaskRestApi.exception.ProjectExistsException;
import com.qelery.TaskRestApi.exception.ProjectNotFoundException;
import com.qelery.TaskRestApi.exception.TaskNotFoundException;
import com.qelery.TaskRestApi.model.Project;
import com.qelery.TaskRestApi.model.User;
import com.qelery.TaskRestApi.model.enums.Priority;
import com.qelery.TaskRestApi.model.enums.Status;
import com.qelery.TaskRestApi.model.Task;
import com.qelery.TaskRestApi.repository.ProjectRepository;
import com.qelery.TaskRestApi.repository.TaskRepository;
import com.qelery.TaskRestApi.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }


    // Project CRUD methods
    public List<Project> getProjects(int limit, String sort) {
        return projectRepository.findAllByUserId(getUser().getId(), getPageable(limit, sort));
    }

    public Project getProject(Long projectId) {
        Project project = projectRepository.findByIdAndUserId(projectId, getUser().getId());
        if (project != null) {
            return project;
        } else {
            throw new ProjectNotFoundException(projectId);
        }
    }

    public Project createProject(Project project) {
        if (projectRepository.existsByNameAndUserId(project.getName(), getUser().getId())) {
            throw new ProjectExistsException(project.getName());
        } else {
            project.setUser(getUser());
            return projectRepository.save(project);
        }
    }

    public Project updateProject(Long projectId, Project newProject) {
        Project oldProject = this.getProject(projectId); // handles ProjectNotFound exception
        oldProject.setName(newProject.getName());
        oldProject.setDescription(newProject.getDescription());
        return projectRepository.save(oldProject);
    }

    public Project partialUpdateProject(Long projectId, Project newProject) {
        Project existingProject = this.getProject(projectId);
        Optional.ofNullable(newProject.getName()).ifPresent(existingProject::setName);
        Optional.ofNullable(newProject.getDescription()).ifPresent(existingProject::setDescription);
        return projectRepository.save(existingProject);
    }

    public ResponseEntity<?> deleteProject(Long projectId) {
        boolean projectExists = projectRepository.existsByIdAndUserId(projectId, getUser().getId());
        if (projectExists) {
            projectRepository.deleteById(projectId);
            String message = "Project with id " + projectId + " deleted";
            return ResponseEntity.ok(message);
        } else {
            throw new ProjectNotFoundException(projectId);
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

    public List<Task> getTasksByProject(Long projectId, Boolean overdue, Priority priority, Status status,
                                        LocalDate dueBefore, LocalDate dueAfter,
                                        int limit, String sort) {
        Project project = this.getProject(projectId); // handles ProjectNotFound exception
        List<Task> tasks = taskRepository.findByProjectId(project.getId(), getPageable(limit, sort));
        tasks = filterTasksByParams(tasks, overdue, priority, status, dueBefore, dueAfter);
        return tasks;
    }

    public Task getTaskByProject(Long projectId, Long taskId) {
        Project project = this.getProject(projectId);
        Optional<Task> optionalTask = project.getTasks()
                .stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst();
        return optionalTask.orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    public Task createTask(Long projectId, Task task) {
        Project project = this.getProject(projectId);
        task.setUser(getUser());
        task.setProject(project);
        return taskRepository.save(task);
    }

    public Task updateTask(Long projectId, Long taskID, Task newTask) {
        Task oldTask = this.getTaskByProject(projectId, taskID); // handles ProjectNotFound and TaskNotFound exceptions
        oldTask.setName(newTask.getName());
        oldTask.setDescription(newTask.getDescription());
        oldTask.setDueDate(newTask.getDueDate());
        oldTask.setStatus(newTask.getStatus());
        return taskRepository.save(oldTask);
    }

    public ResponseEntity<?> markTaskComplete(Long projectId, Long taskId) {
        Task task = this.getTaskByProject(projectId, taskId);
        task.setStatus(Status.COMPLETED);
        taskRepository.save(task);
        return new ResponseEntity<>("Marked task complete", HttpStatus.OK);
    }

    public Task partialUpdateTask(Long projectId, Long taskId, Task newTask) {
        Task existingTask = this.getTaskByProject(projectId, taskId);
        Optional.ofNullable(newTask.getName()).ifPresent(existingTask::setName);
        Optional.ofNullable(newTask.getDescription()).ifPresent(existingTask::setDescription);
        Optional.ofNullable(newTask.getDueDate()).ifPresent(existingTask::setDueDate);
        Optional.ofNullable(newTask.getStatus()).ifPresent(existingTask::setStatus);
        return taskRepository.save(existingTask);
    }

    public ResponseEntity<?> deleteTask(Long projectId, Long taskId) {
        Task task = this.getTaskByProject(projectId, taskId);
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