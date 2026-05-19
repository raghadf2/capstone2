package com.example.spaceflow.Controller;

import com.example.spaceflow.Api.ApiResponse;
import com.example.spaceflow.Model.Category;
import com.example.spaceflow.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody @Valid Category category,@RequestParam Integer userId){
        categoryService.addCategory(category,userId);
        return ResponseEntity.status(200).body(new ApiResponse("category Added"));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getCategory(){
        return ResponseEntity.status(200).body(categoryService.getCategory());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer id, @RequestBody @Valid Category category, @RequestParam Integer userId){
        categoryService.updateCategory(id,category,userId);
        return ResponseEntity.status(200).body(new ApiResponse("category Updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id,@RequestParam Integer userId){
        categoryService.deleteCategory(id,userId);
        return ResponseEntity.status(200).body(new ApiResponse("category Deleted"));
    }
}
