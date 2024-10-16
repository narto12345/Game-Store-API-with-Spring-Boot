package com.bitsteam.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bitsteam.app.entities.Gender;
import com.bitsteam.app.services.GenderService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/gender")
public class GenderController {

	private GenderService genderService;

	public GenderController(GenderService genderService) {
		this.genderService = genderService;
	}

	@GetMapping
	public List<Gender> list() {
		return genderService.getAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		Optional<Gender> genderOptional = this.genderService.getById(id);

		if (genderOptional.isPresent())
			return ResponseEntity.ok(genderOptional);
		else
			return ResponseEntity.notFound().build();
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> create(@Valid @RequestBody Gender gender, BindingResult result) {
		if (result.hasFieldErrors()) {
			return this.validation(result);
		}

		Gender genderCreated = this.genderService.save(gender);
		return ResponseEntity.ok(genderCreated);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> update(@Valid @RequestBody Gender gender, BindingResult result, @PathVariable Long id) {
		if (result.hasFieldErrors()) {
			return this.validation(result);
		}

		Optional<Gender> genderOptional = this.genderService.getById(id);
		if (genderOptional.isPresent()) {
			gender.setId(id);
			Gender genderCreated = this.genderService.save(gender);
			return ResponseEntity.ok(genderCreated);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Gender> delete(@PathVariable Long id) {
		Optional<Gender> genderOptional = this.genderService.getById(id);

		if (genderOptional.isPresent()) {
			Optional<Gender> genderDeleted = this.genderService.delete(genderOptional.get());
			return ResponseEntity.ok(genderDeleted.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	private ResponseEntity<?> validation(BindingResult result) {
		Map<String, Object> errors = new HashMap<>();
		result.getFieldErrors().forEach(error -> errors.put(error.getField(),
				"El campo " + error.getField() + " " + error.getDefaultMessage()));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}

}
