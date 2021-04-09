package com.qelery.TaskRestApi.repository;

import com.qelery.TaskRestApi.model.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCategoryId(Long categoryId, Pageable pageable);
    List<Task> findAllByUserId(Long userId, Pageable pageable);
}