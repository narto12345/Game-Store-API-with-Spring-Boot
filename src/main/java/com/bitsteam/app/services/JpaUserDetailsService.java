package com.bitsteam.app.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitsteam.app.entities.User;
import com.bitsteam.app.repositories.UserRepository;

@Service
public class JpaUserDetailsService
implements UserDetailsService 
{

	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println(".......");
		Optional<User> userOptional = this.userRepository.findByUsernameWithRoles(username);

		if (userOptional.isEmpty()) {
			throw new UsernameNotFoundException("user not find");
		}

		User user = userOptional.get();

		List<GrantedAuthority> grantedAuthorities = user.getRoles().stream().map(role -> {
			return new SimpleGrantedAuthority(role.getName());
		}).collect(Collectors.toList());

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				user.isEnable(), true, true, true, grantedAuthorities);
	}

}
