package com.qelery.TodoRestApi.repository;


import com.qelery.TodoRestApi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Boolean existsByName(String categoryName);
}