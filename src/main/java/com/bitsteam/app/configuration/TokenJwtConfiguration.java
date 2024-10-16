package com.bitsteam.app.configuration;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

public class TokenJwtConfiguration {
	protected static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
	protected static final String HEADER_AUTHORIZATION = "Authorization";
	protected static final String PREFIX_TOKEN = "Bearer ";
	protected static final String CONTENT_TYPE_JSON = "application/json";
}
