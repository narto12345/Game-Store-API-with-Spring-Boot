package com.bitsteam.app.services;

import java.util.List;
import java.util.Optional;

import com.bitsteam.app.entities.Gender;

public interface GenderService {
	
	public abstract List<Gender> getAll();
	
	public abstract Optional<Gender> getById(Long id);
	
	public abstract Gender save(Gender gender);
	
	public abstract Optional<Gender> delete(Gender gender);
	
}
