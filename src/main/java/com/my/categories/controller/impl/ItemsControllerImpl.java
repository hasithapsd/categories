package com.my.categories.controller.impl;

import com.my.categories.controller.ItemsAPI;
import com.my.categories.data.model.Category;
import com.my.categories.data.model.Item;
import com.my.categories.repository.CategoryRepository;
import com.my.categories.repository.ItemRepository;
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
public class ItemsControllerImpl implements ItemsAPI {

    private static final Logger LOG = LoggerFactory.getLogger(ItemsAPI.class);

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public ResponseEntity<Item> createTutorial(@RequestBody Item item) {
        try {

            Optional<Category> category1 = categoryRepository.findById(item.getCategoryId());
            if(category1.isPresent()){
                Item item1 = itemRepository.save(Item.builder().categoryId(category1.get().getId()).attributes(item.getAttributes()).build());
                return new ResponseEntity<>(item1, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Item> getTutorialById(@PathVariable("id") String id) {
        Optional<Item> itemOptional = itemRepository.findById(id);

        if (itemOptional.isPresent()) {
            return new ResponseEntity<>(itemOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<List<Item>> getAllItems(@RequestParam(required = false) String categoryId) {
        try {
            List<Item> items = new ArrayList<Item>();

            if (categoryId == null)
                itemRepository.findAll().forEach(items::add);
            else
                itemRepository.findByCategoryId(categoryId).forEach(items::add);

            if (items.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
