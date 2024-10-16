package com.bitsteam.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bitsteam.app.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

	@Query("select u from User u join fetch u.roles r")
	List<User> findAllWithRoles();

	boolean existsByUsername(String username);

	@Query("select u from User u join fetch u.roles r where u.username = ?1")
	Optional<User> findByUsernameWithRoles(String username);

}
