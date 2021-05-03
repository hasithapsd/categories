package com.my.categories.repository;

import com.my.categories.data.model.Item;
import com.my.categories.data.model.Tutorial;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ItemRepository extends MongoRepository<Item, String> {
    List<Item> findByCategoryId(String categoryId);

}