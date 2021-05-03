package com.my.categories.controller.impl;

import com.my.categories.controller.CategoryAPI;
import com.my.categories.data.model.Category;
import com.my.categories.repository.CategoryRepository;
import com.my.categories.utils.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("api/v1")
public class CategoryControllerImpl implements CategoryAPI {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryControllerImpl.class);


    @Autowired
    CategoryRepository categoryRepo;

    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        LOG.info("Request Received for category creation | category request: {}", JsonConverter.getJsonString(category));
        try {
            Category category1 = categoryRepo.save(Category.builder().
                    description(category.getDescription()).
                    title(category.getTitle()).
                    name(category.getName()).
                    attributes(category.getAttributes())
                    .build());
            return new ResponseEntity<>(category1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Category> getCategoryById(@PathVariable("id") String id) {
        Optional<Category> categoryOptional = categoryRepo.findById(id);

        if (categoryOptional.isPresent()) {
            return new ResponseEntity<>(categoryOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<List<Category>> getAllCategories() {
        try {
            List<Category> tutorials = new ArrayList<Category>();

            categoryRepo.findAll().forEach(tutorials::add);

            if (tutorials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(tutorials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
