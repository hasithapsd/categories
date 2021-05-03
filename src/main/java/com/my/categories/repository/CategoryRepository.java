package com.my.categories.repository;

import com.my.categories.data.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findByTitleContaining(String title);
}
