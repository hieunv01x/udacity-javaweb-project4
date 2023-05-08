package com.example.demo.controllersTest;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp () {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void get_items () {
        ResponseEntity<List<Item>> items = itemController.getItems();
        Assertions.assertNotNull(items);
        Assertions.assertEquals(200, items.getStatusCodeValue());
    }

    @Test
    public void get_items_by_name () {
        List<Item> i = new ArrayList<>();
        i.add(new Item());
        when(itemRepository.findByName("Round Widget")).thenReturn(i);
        ResponseEntity<List<Item>> items = itemController.getItemsByName("Round Widget");
        Assertions.assertNotNull(items);
        Assertions.assertEquals(200, items.getStatusCodeValue());
        Assertions.assertEquals(1, items.getBody()!= null ? items.getBody().size(): 0);
    }

    @Test
    public void get_items_by_invalid_name () {
        List<Item> i = new ArrayList<>();
        when(itemRepository.findByName("missing Widget")).thenReturn(i);
        ResponseEntity<List<Item>> items = itemController.getItemsByName("missing Widget");
        Assertions.assertNotNull(items);
        Assertions.assertEquals(404, items.getStatusCodeValue());
    }

    @Test
    public void get_items_by_id () {
        Item i = new Item();
        i.setId(1L);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(i));
        ResponseEntity<Item> item = itemController.getItemById(1L);
        Assertions.assertNotNull(item);
        Assertions.assertEquals(200, item.getStatusCodeValue());
    }

    @Test
    public void get_items_by_invalid_id () {
        when(itemRepository.findById(2L)).thenReturn(Optional.ofNullable(null));
        ResponseEntity<Item> item = itemController.getItemById(2L);
        Assertions.assertNotNull(item);
        Assertions.assertEquals(404, item.getStatusCodeValue());
    }
}
