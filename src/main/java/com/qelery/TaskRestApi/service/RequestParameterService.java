package com.qelery.TaskRestApi.service;

import com.qelery.TaskRestApi.model.Task;
import com.qelery.TaskRestApi.model.enums.Priority;
import com.qelery.TaskRestApi.model.enums.Status;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestParameterService {

    public List<Task> filterTasksByParams(List<Task> tasks, Boolean overdue,
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
}
