package com.example.spaceflow.Service;

import com.example.spaceflow.Api.ApiException;
import com.example.spaceflow.Model.Category;
import com.example.spaceflow.Model.User;
import com.example.spaceflow.Repository.CategoryRepository;
import com.example.spaceflow.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserService userService;

    //CRUD
    public void addCategory(Category category, Integer userId){
        userService.checkUserRole(userId, "ADMIN");

        if(categoryRepository.findCategoryByName(category.getName()) != null){
            throw new ApiException("Category Already Exists");
        }

        categoryRepository.save(category);
    }

    public List<Category> getCategory(){
        return categoryRepository.findAll();
    }

    public void updateCategory(Integer id, Category category, Integer userId){
        userService.checkUserRole(userId, "ADMIN");

        Category old = categoryRepository.findCategoriesById(id);

        if(old == null){
            throw new ApiException("Category Not Exists");
        }

        old.setName(category.getName());
        old.setDescription(category.getDescription());

        categoryRepository.save(old);
    }

    public void deleteCategory(Integer id, Integer userId){
        userService.checkUserRole(userId, "ADMIN");
        Category exists = categoryRepository.findCategoriesById(id);

        if(exists == null){
            throw new ApiException("Category Not Exists");
        }

        categoryRepository.delete(exists);
    }

}
