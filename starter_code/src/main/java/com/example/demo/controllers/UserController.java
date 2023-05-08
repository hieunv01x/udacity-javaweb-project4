package com.example.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/user")
public class UserController {

	Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		try {
			User user = userRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Can't find User with id"));
			return ResponseEntity.ok(user);
		} catch (EntityNotFoundException e) {
			log.error("Can't find User with id:", new EntityNotFoundException());
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("Can't find User with name", new EntityNotFoundException());
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> createUser(@RequestBody CreateUserRequest createUserRequest) {
		if (userRepository.findByUsername(createUserRequest.getUsername()) != null) {
			log.info("Username set with {} is already taken", createUserRequest.getUsername());
			return ResponseEntity.badRequest().body("Error: Username is already taken!");
		}

		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		if(createUserRequest.getPassword() == null || createUserRequest.getPassword().length()<=6 ||
				!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			log.debug("An error occurred with the user's password. Can not create user {}", createUserRequest.getUsername());
			return ResponseEntity.badRequest().body("An error occurred with the user's password");
		}
		user.setPassword(encoder.encode(createUserRequest.getPassword()));
		userRepository.save(user);
		log.info("User created username {} successfully!", createUserRequest.getUsername());
		return ResponseEntity.ok(user);
	}
	
}
