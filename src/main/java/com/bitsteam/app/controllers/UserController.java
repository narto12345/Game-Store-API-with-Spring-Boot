package com.bitsteam.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bitsteam.app.entities.User;
import com.bitsteam.app.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public List<User> list() {
		return userService.getAll();
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult result) {
		if (result.hasFieldErrors()) {
			return this.validation(result);
		}

		user.setAdmin(false);
		User userCreated = this.userService.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
		if (result.hasFieldErrors()) {
			return this.validation(result);
		}

		User userCreated = this.userService.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
	}

	private ResponseEntity<?> validation(BindingResult result) {
		Map<String, Object> errors = new HashMap<>();
		result.getFieldErrors().forEach(error -> errors.put(error.getField(),
				"El campo " + error.getField() + " " + error.getDefaultMessage()));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}

}
