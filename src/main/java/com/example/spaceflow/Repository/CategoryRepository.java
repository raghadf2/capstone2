package com.example.spaceflow.Repository;

import com.example.spaceflow.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {
    Category findCategoriesById(Integer id);
    Category findCategoryByName(String name);
}
