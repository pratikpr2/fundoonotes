package com.bridgelabz.fundoonotes.user.token;

import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.user.exception.TokenParsingException;
import com.bridgelabz.fundoonotes.user.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtToken {

	@Value("${mykey}")
	String KEY;

	/**
	 * Method to create token
	 * @param emp
	 * @return JWT token
	 */
	public String createJWT(User user) {
		
		String subject = user.getUserId();
		String issuer = user.getUserName();
		Date now = new Date();
		
		JwtBuilder builder = Jwts.builder().setIssuedAt(now).setSubject(subject).setIssuer(issuer)
				.signWith(SignatureAlgorithm.HS256, KEY);

		
		return builder.compact();
	}

	/**
	 * Method to access token
	 * @param jwt
	 * @throws TokenParsingException 
	 */
	public void parseJWT(String jwt) throws TokenParsingException{
		Claims claims;
		
		try {
		    	 claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(KEY)).parseClaimsJws(jwt)
				.getBody();
		    }
		      catch(Exception exception) {
		    	  throw new TokenParsingException("Malformed Token");
		      }
		      
		
	}
	
	public String getUserId(String jwt) throws TokenParsingException {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(KEY)).parseClaimsJws(jwt)
					.getBody();
		}
		catch (Exception e) {
			throw new TokenParsingException("Malformed Token");
		}
		return  claims.getSubject();
	}
	
	
}
