package com.bitsteam.app.configuration;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Arrays;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.bitsteam.app.configuration.TokenJwtConfiguration.*;

public class JwtValidationFilter extends BasicAuthenticationFilter {

	public JwtValidationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String hearder = request.getHeader(HEADER_AUTHORIZATION);
		Map<String, Object> errorBody = new HashMap<>();

		if (hearder == null || !hearder.startsWith(PREFIX_TOKEN)) {
			chain.doFilter(request, response);
			return;
		}

		String token = hearder.replace(PREFIX_TOKEN, "");

		try {
			Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
			String username = claims.getSubject();
			Object authoritiesClaims = claims.get("authorities");
			Collection<? extends GrantedAuthority> authorities = Arrays.asList(
					new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
							.readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class));

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
					null, authorities);
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			chain.doFilter(request, response);
		} catch (JwtException ex) {
			errorBody.put("error", ex.getMessage());
			response.setContentType(CONTENT_TYPE_JSON);
			response.setStatus(401);
			response.getWriter().write(new ObjectMapper().writeValueAsString(errorBody));
		}

	}

}
