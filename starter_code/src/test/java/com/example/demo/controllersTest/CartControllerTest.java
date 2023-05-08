package com.example.demo.controllersTest;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void modify_cart_request() {
        User user = getTestUser(1L, "hieu");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(getTestUser(1L, "hieu"));
        when(cartRepository.save(any())).thenReturn(new Cart());
        Optional<Item> itemOptional = Optional.of(new Item());
        itemOptional.get().setPrice(BigDecimal.valueOf(10.0));
        when(itemRepository.findById(any())).thenReturn(itemOptional);
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername(user.getUsername());
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();
        Assertions.assertNotNull(cart);
        Assertions.assertEquals(10, cart.getTotal().intValue());
    }

    @Test
    public void add_cart_with_user_not_found() {
        User user = getTestUser(1L, "hieu");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(getTestUser(1L, "hieu"));

        when(cartRepository.save(any())).thenReturn(new Cart());
        Optional<Item> itemOptional = Optional.of(new Item());
        itemOptional.get().setPrice(BigDecimal.valueOf(6.0));
        when(itemRepository.findById(any())).thenReturn(itemOptional);
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(2);

        // Username is not found in database
        modifyCartRequest.setUsername("UserName_Not_found");
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void add_cart_with_item_not_found() {
        User user = getTestUser(1L, "hieu");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(getTestUser(1L, "hieu"));

        when(itemRepository.findById(1L)).thenReturn(Optional.of(getItem()));
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();

        // Item is not found in database
        modifyCartRequest.setItemId(2);
        modifyCartRequest.setQuantity(2);
        modifyCartRequest.setUsername("hieu");
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void remove_cart_with_user_not_found() {
        User user = getTestUser(1L, "hieu");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(getTestUser(1L, "hieu"));

        when(cartRepository.save(any())).thenReturn(new Cart());
        Optional<Item> itemOptional = Optional.of(new Item());
        itemOptional.get().setPrice(BigDecimal.valueOf(6.0));
        when(itemRepository.findById(any())).thenReturn(itemOptional);
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(2);

        // Username is not found in database
        modifyCartRequest.setUsername("UserName_Not_found");
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_cart_with_item_not_found() {
        User user = getTestUser(1L, "hieu");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(getTestUser(1L, "hieu"));

        when(itemRepository.findById(1L)).thenReturn(Optional.of(getItem()));
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();

        // Item is not found in database
        modifyCartRequest.setItemId(2);

        modifyCartRequest.setQuantity(2);
        modifyCartRequest.setUsername("hieu");
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(404, response.getStatusCodeValue());
    }

    public static User getTestUser(long id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword("password");
        user.setCart(new Cart());
        return user;
    }

    public static Item getItem() {
        Item item = new Item();
        item.setId(1L);
        item.setPrice(BigDecimal.valueOf(3.0));
        item.setDescription("Item Test");
        return item;
    }

}
