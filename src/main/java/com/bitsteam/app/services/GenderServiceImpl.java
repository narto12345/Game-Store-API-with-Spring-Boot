package com.bitsteam.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitsteam.app.entities.Gender;
import com.bitsteam.app.repositories.GenderRepository;

@Service
public class GenderServiceImpl implements GenderService {

	private GenderRepository genderRepository;

	public GenderServiceImpl(GenderRepository genderRepository) {
		this.genderRepository = genderRepository;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Gender> getAll() {
		return (List<Gender>) this.genderRepository.findAll();
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<Gender> getById(Long id) {
		return this.genderRepository.findById(id);
	}

	@Transactional
	@Override
	public Gender save(Gender gender) {
		return this.genderRepository.save(gender);
	}

	@Transactional
	@Override
	public Optional<Gender> delete(Gender gender) {
		Optional<Gender> genderOptional = this.genderRepository.findById(gender.getId());
		genderOptional.ifPresent(LambdaGender -> {
			this.genderRepository.delete(LambdaGender);
		});
		return genderOptional;
	}

}
