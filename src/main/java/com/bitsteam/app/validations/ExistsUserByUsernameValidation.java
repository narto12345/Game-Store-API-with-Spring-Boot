package com.bitsteam.app.validations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bitsteam.app.services.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistsUserByUsernameValidation implements ConstraintValidator<ExistsUserByUsername, String> {

	@Autowired
	private UserService userService;

	public ExistsUserByUsernameValidation() {

	}

	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		if (userService != null)
			return !this.userService.existsByUsername(username);
		else
			return true;

	}

}
