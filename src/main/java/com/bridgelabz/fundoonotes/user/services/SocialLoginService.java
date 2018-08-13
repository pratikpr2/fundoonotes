package com.bridgelabz.fundoonotes.user.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.bridgelabz.fundoonotes.user.model.FacebookUser;
import com.bridgelabz.fundoonotes.user.model.User;
import com.bridgelabz.fundoonotes.user.repository.UserRepository;
import com.bridgelabz.fundoonotes.user.token.JwtToken;

@Service
public class SocialLoginService {

	@Value("${spring.social.facebook.appId}")
	String facebookAppId;
	@Value("${spring.social.facebook.appSecret}")
	String facebookSecret;

	@Autowired 
	UserRepository mongoRepo;
	
	@Autowired
	ModelMapper mapper;
	
	@Autowired
	JwtToken jwt;
	
	private String accessToken;

	public String createFacebookAuthorizationURL() {
		FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(facebookAppId, facebookSecret);
		OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
		OAuth2Parameters params = new OAuth2Parameters();
		params.setRedirectUri("http://localhost:8080/swagger-ui.html#!/facebook-login-controller/createFacebookAccessTokenUsingGET");
		params.setScope("public_profile,email,user_birthday");
		return oauthOperations.buildAuthorizeUrl(params);
	}
	
	public String createFacebookAccessToken(String code) {
	    FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(facebookAppId, facebookSecret);
	    AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeForAccess(code, "http://localhost:8080/swagger-ui.html#!/facebook-login-controller/createFacebookAccessTokenUsingGET", null);
	    accessToken = accessGrant.getAccessToken();
	    
	    FacebookUser user = getName();
	    User regUser = new User();
	    
	    if(!mongoRepo.findByUserEmail(user.getEmail()).isPresent()) {
	    	
	    	regUser.setUserEmail(user.getEmail());
	    	regUser.setStatus(true);
	    	mongoRepo.save(regUser);
	    }
	    return jwt.createJWT(regUser);
	    
	}

	public FacebookUser getName() {
		Facebook facebook = new FacebookTemplate(accessToken);

		FacebookUser user = facebook.fetchObject("me", FacebookUser.class, "email");

		return user;
	   
	}
}
