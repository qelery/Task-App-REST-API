package com.qelery.TaskRestApi.repository;


import com.qelery.TaskRestApi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Boolean existsByName(String categoryName);
}