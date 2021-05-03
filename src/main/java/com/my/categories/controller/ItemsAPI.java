package com.my.categories.controller;

import com.my.categories.data.model.Item;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(value = "items")
public interface ItemsAPI {

    @PostMapping("/items")
    public ResponseEntity<Item> createTutorial(@RequestBody Item item) ;
    @GetMapping("/items/{id}")
    public ResponseEntity<Item> getTutorialById(@PathVariable("id") String id);
    public ResponseEntity<List<Item>> getAllItems(@RequestParam(required = false) String categoryId);
}
