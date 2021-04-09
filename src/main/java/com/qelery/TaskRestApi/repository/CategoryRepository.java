package com.qelery.TaskRestApi.repository;


import com.qelery.TaskRestApi.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByNameAndUserId(String categoryName, Long userId);
    boolean existsByIdAndUserId(Long categoryId, Long userId);
    List<Category> findAllByUserId(Long userId, Pageable pageable);
    Category findByIdAndUserId(Long categoryId, Long userId);
}