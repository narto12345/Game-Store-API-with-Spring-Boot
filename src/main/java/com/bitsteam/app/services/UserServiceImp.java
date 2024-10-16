package com.bitsteam.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitsteam.app.entities.Role;
import com.bitsteam.app.entities.User;
import com.bitsteam.app.repositories.RoleRepository;
import com.bitsteam.app.repositories.UserRepository;

@Service
public class UserServiceImp implements UserService {

	private UserRepository userRepository;

	private RoleRepository roleRepository;

	private PasswordEncoder passwordEncoder;

	public UserServiceImp(UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional(readOnly = true)
	@Override
	public List<User> getAll() {
		return this.userRepository.findAllWithRoles();
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<User> getById(Long id) {
		return this.userRepository.findById(id);
	}

	@Transactional
	@Override
	public User save(User user) {
		List<Role> roles = new ArrayList<>();

		Optional<Role> optionalRoleUser = this.roleRepository.findByName("ROLE_USER");
		if (optionalRoleUser.isPresent()) {
			roles.add(optionalRoleUser.get());
		}

		if (user.isAdmin()) {
			Optional<Role> optionalRoleAdmin = this.roleRepository.findByName("ROLE_ADMIN");
			if (optionalRoleAdmin.isPresent()) {
				roles.add(optionalRoleAdmin.get());
			}
		}

		user.setRoles(roles);
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		return this.userRepository.save(user);
	}

	@Transactional
	@Override
	public Optional<User> delete(User user) {
		Optional<User> userOptional = this.userRepository.findById(user.getId());
		userOptional.ifPresent(lambdaUser -> {
			this.userRepository.delete(lambdaUser);
		});
		return userOptional;
	}

	@Override
	public boolean existsByUsername(String username) {
		return this.userRepository.existsByUsername(username);
	}

}
