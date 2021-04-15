package com.qelery.TaskRestApi.controller;

import com.qelery.TaskRestApi.model.Project;
import com.qelery.TaskRestApi.model.Task;
import com.qelery.TaskRestApi.model.enums.Priority;
import com.qelery.TaskRestApi.model.enums.Status;
import com.qelery.TaskRestApi.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path="/api")
public class ProjectController {

    private ProjectService projectService;

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }


    // Project endpoints
    @GetMapping("/projects")
    public List<Project> getProjects(@RequestParam(defaultValue="100") int limit,
                                     @RequestParam(defaultValue="name,asc") String sort) {
        return projectService.getProjects(limit, sort);
    }

    @GetMapping("/projects/{projectId}")
    public Project getProject(@PathVariable Long projectId) {
        return projectService.getProject(projectId);
    }

    @PostMapping("/projects")
    public Project createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    @PutMapping("/projects/{projectId}")
    public Project updateProject(@PathVariable Long projectId, @RequestBody Project project) {
        return projectService.updateProject(projectId, project);
    }

    @PatchMapping("/projects/{projectId}")
    public Project partialUpdateProject(@PathVariable Long projectId,
                                        @RequestBody Project project) {
        return projectService.partialUpdateProject(projectId, project);
    }

    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable Long projectId) {
        return projectService.deleteProject(projectId);
    }


    // Task endpoints
    @GetMapping("/projects/tasks/all")
    public List<Task> getAllTasks(@RequestParam(required=false) Boolean overdue,
                                  @RequestParam(required=false) Priority priority,
                                  @RequestParam(required=false) Status status,
                                  @RequestParam(required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueBefore,
                                  @RequestParam(required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueAfter,
                                  @RequestParam(defaultValue="100") int limit,
                                  @RequestParam(defaultValue="dueDate,desc") String sort) {
        return projectService.getAllTasks(overdue, priority, status, dueBefore, dueAfter, limit, sort);
    }


    @GetMapping("/projects/{projectId}/tasks")
    public List<Task> getTasksByProject(@PathVariable Long projectId,
                                        @RequestParam(required=false) Boolean overdue,
                                        @RequestParam(required=false) Priority priority,
                                        @RequestParam(required=false) Status status,
                                        @RequestParam(required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueBefore,
                                        @RequestParam(required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueAfter,
                                        @RequestParam(defaultValue="100") int limit,
                                        @RequestParam(defaultValue="dueDate,desc") String sort) {
        return projectService.getTasksByProject(projectId, overdue, priority, status, dueBefore, dueAfter, limit, sort);
    }

    @GetMapping("/projects/{projectId}/tasks/{taskId}")
    public Task getTaskByProject(@PathVariable Long projectId,
                                 @PathVariable Long taskId) {
        return projectService.getTaskByProject(projectId, taskId);
    }

    @PostMapping("/projects/{projectId}/tasks")
    public Task createTask(@PathVariable Long projectId,
                           @RequestBody Task task) {
        return projectService.createTask(projectId, task);
    }

    @PutMapping("/projects/{projectId}/tasks/{taskId}")
    public Task updateTask(@PathVariable Long projectId,
                           @PathVariable Long taskId,
                           @RequestBody Task task) {
        return projectService.updateTask(projectId, taskId, task);
    }

    @PutMapping("/projects/{projectId}/tasks/{taskId}/complete")
    public ResponseEntity<?> markTaskComplete(@PathVariable Long projectId,
                                              @PathVariable Long taskId) {
        return projectService.markTaskComplete(projectId, taskId);
    }

    @PatchMapping("/projects/{projectId}/tasks/{taskId}")
    public Task partialUpdateTask(@PathVariable Long projectId,
                                  @PathVariable Long taskId,
                                  @RequestBody Task task) {
        return projectService.partialUpdateTask(projectId, taskId, task);
    }

    @DeleteMapping("/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long projectId,
                                        @PathVariable Long taskId) {
        return projectService.deleteTask(projectId, taskId);
    }
}
