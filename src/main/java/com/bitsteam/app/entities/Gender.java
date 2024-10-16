package com.bitsteam.app.entities;

import com.bitsteam.app.validations.IsRequired;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
//import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "genres")
public class Gender {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

//	@NotEmpty(message = "{NotEmpty.gender.name}")
	@IsRequired
	@Column(name = "name")
	private String name;

	public Gender() {
	}

	public Gender(String name) {
		super();
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
