package com.bitsteam.app.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bitsteam.app.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	public abstract Optional<Role> findByName(String name);

}
