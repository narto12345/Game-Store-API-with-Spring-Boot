package com.bitsteam.app.configuration;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bitsteam.app.entities.User;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.bitsteam.app.configuration.TokenJwtConfiguration.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		User user = null;
		String username = null;
		String password = null;

		try {
			user = new ObjectMapper().readValue(request.getInputStream(), User.class);
			username = user.getUsername();
			password = user.getPassword();
		} catch (StreamReadException e) {
			e.printStackTrace();
		} catch (DatabindException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);
		return authenticationManager.authenticate(authenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String username = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername();
		Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
		System.out.println("roles: " + roles.size());
		Claims claims = Jwts.claims()
				.add("authorities", new ObjectMapper().writeValueAsString(roles)).build();

		String jwtToken = Jwts.builder().subject(username).claims(claims)
				.expiration(new Date(System.currentTimeMillis() + 3600000)).issuedAt(new Date()).signWith(SECRET_KEY)
				.compact();
		response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + jwtToken);

		Map<String, Object> jsonResponse = new HashMap<>();

		jsonResponse.put("username", username);
		jsonResponse.put("token", jwtToken);

		response.setContentType(CONTENT_TYPE_JSON);
		response.setStatus(200);
		response.getWriter().write(new ObjectMapper().writeValueAsString(jsonResponse));
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		Map<String, Object> jsonResponse = new HashMap<>();

		jsonResponse.put("message", "authentication error");

		response.setContentType(CONTENT_TYPE_JSON);
		response.setStatus(401);
		response.getWriter().write(new ObjectMapper().writeValueAsString(jsonResponse));
	}

}
