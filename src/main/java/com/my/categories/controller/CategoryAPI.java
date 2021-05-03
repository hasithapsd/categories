package com.my.categories.controller;

import com.my.categories.data.model.Category;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "categories")
public interface CategoryAPI {


    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody Category category);

    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") String id);

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories();
}
