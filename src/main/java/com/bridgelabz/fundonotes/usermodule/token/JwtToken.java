package com.bridgelabz.fundonotes.usermodule.token;

import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import com.bridgelabz.fundonotes.usermodule.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtToken {

	final static String KEY = "PRATIK";

	/**
	 * Method to create token
	 * @param emp
	 * @return JWT token
	 */
	public String createJWT(User user) {
		// The JWT signature algorithm we will be using to sign the token

		String subject = user.getUserEmail();
		String issuer = user.getUserName();
		Date now = new Date();
		// Let's set the JWT Claims
		JwtBuilder builder = Jwts.builder().setIssuedAt(now).setSubject(subject).setIssuer(issuer)
				.signWith(SignatureAlgorithm.HS256, KEY);

		// Builds the JWT and serializes it to a compact, URL-safe string
		return builder.compact();
	}

	/**
	 * Method to access token
	 * @param jwt
	 */
	public void parseJWT(String jwt) {

		// This line will throw an exception if it is not a signed JWS (as expected)
		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(KEY)).parseClaimsJws(jwt)
				.getBody();
		//System.out.println("ID: " + claims.getId());
		System.out.println("Subject: " + claims.getSubject());
		System.out.println("Issuer: " + claims.getIssuer());
		System.out.println("Expiration: " + claims.getExpiration());
}
}
