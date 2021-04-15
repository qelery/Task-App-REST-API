package com.qelery.TaskRestApi.repository;


import com.qelery.TaskRestApi.model.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    boolean existsByNameAndUserId(String projectName, Long userId);
    boolean existsByIdAndUserId(Long projectId, Long userId);
    List<Project> findAllByUserId(Long userId, Pageable pageable);
    Project findByIdAndUserId(Long projectId, Long userId);
}