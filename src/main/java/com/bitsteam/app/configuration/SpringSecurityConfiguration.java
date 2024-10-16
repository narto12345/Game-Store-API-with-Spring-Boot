package com.bitsteam.app.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfiguration {

	@Autowired
	private AuthenticationConfiguration authenticationConfiguration;

	@Bean
	AuthenticationManager authenticationManager() throws Exception {
		return this.authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.authorizeHttpRequests(authz ->
				authz
				//public resources added
				.requestMatchers(HttpMethod.POST, "/api/user/register").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/user").permitAll()
				
				//rules added
				//.requestMatchers(HttpMethod.POST, "/api/user").hasRole("ADMIN")
				//.requestMatchers(HttpMethod.POST, "/api/gender").hasRole("ADMIN")
				//.requestMatchers(HttpMethod.PUT, "/api/gender/{id}").hasRole("ADMIN")
				//.requestMatchers(HttpMethod.DELETE, "/api/gender/{id}").hasRole("ADMIN")
				
						.anyRequest().authenticated())	
				
				.addFilter(new JwtAuthenticationFilter(this.authenticationManager()))
				.addFilter(new JwtValidationFilter(this.authenticationManager()))
				.csrf(config -> config.disable())
				.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.build();
	}

}
