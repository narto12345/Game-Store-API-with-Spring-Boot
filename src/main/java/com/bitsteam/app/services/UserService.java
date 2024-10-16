package com.bitsteam.app.services;

import java.util.List;
import java.util.Optional;

import com.bitsteam.app.entities.User;

public interface UserService {

	public abstract List<User> getAll();

	public abstract Optional<User> getById(Long id);

	public abstract User save(User user);

	public abstract Optional<User> delete(User user);
	
	public abstract boolean existsByUsername(String username);

}
