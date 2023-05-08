package com.example.demo.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    Logger log = LoggerFactory.getLogger(ItemController.class);
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping
    public ResponseEntity<List<Item>> getItems() {
        List<Item> items = itemRepository.findAll();
        if (items.isEmpty()) {
            log.info("No item exist!");
        }
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        try {
            Item item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Can't find Item with this ID"));
            return ResponseEntity.ok(item);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage(), new EntityNotFoundException());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
        List<Item> items = itemRepository.findByName(name);
        if (items.isEmpty()) {
            log.error("Can't find Item with this Name", new EntityNotFoundException());
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(items);
    }

}
