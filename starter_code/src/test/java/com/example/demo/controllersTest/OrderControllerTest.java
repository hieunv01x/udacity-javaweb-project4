package com.example.demo.controllersTest;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demo.controllersTest.CartControllerTest.getItem;
import static com.example.demo.controllersTest.CartControllerTest.getTestUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void get_oder_by_username() {
        User user = getTestUser(1L, "hieu");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(getTestUser(1L, "hieu"));
        Optional<UserOrder> userOrder = Optional.of(new UserOrder());
        when(orderRepository.findById(any())).thenReturn(userOrder);
        ResponseEntity<List<UserOrder>> responseOder = orderController.getOrdersForUser("hieu");
        Assertions.assertNotNull(responseOder);
        Assertions.assertEquals(200, responseOder.getStatusCodeValue());
    }

    @Test
    public void get_oder_by_username_not_found() {
        User user = getTestUser(1L, "hieu");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(getTestUser(1L, "hieu"));
        Optional<UserOrder> userOrder = Optional.of(new UserOrder());
        when(orderRepository.findById(any())).thenReturn(userOrder);
        ResponseEntity<List<UserOrder>> responseOder = orderController.getOrdersForUser("username_not_found");
        Assertions.assertNotNull(responseOder);
        Assertions.assertEquals(404, responseOder.getStatusCodeValue());
    }

    @Test
    public void submit_oder_by_username_not_found() {
        User user = getTestUser(1L, "hieu");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(getTestUser(1L, "hieu"));
        Optional<UserOrder> userOrder = Optional.of(new UserOrder());
        when(orderRepository.findById(any())).thenReturn(userOrder);
        ResponseEntity<?> responseOder = orderController.submit("username_not_found");
        Assertions.assertNotNull(responseOder);
        Assertions.assertEquals(404, responseOder.getStatusCodeValue());
    }

    @Test
    public void submit_oder_by_username_successfully() {
        User user = getTestUser(1L, "hieu");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(getTestUser(1L, "hieu"));

        Optional<UserOrder> userOrder = Optional.of(new UserOrder());
        when(orderRepository.findById(any())).thenReturn(userOrder);
        Item item = getItem();
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setTotal(BigDecimal.valueOf(3.0));
        cart.setItems((itemList));
        user.setCart(cart);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        ResponseEntity<?> responseOder = orderController.submit("hieu");
        Assertions.assertNotNull(responseOder);
        Assertions.assertEquals(200, responseOder.getStatusCodeValue());
    }
}
