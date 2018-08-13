package com.bridgelabz.fundoonotes.user.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.user.model.ResponseDto;
import com.bridgelabz.fundoonotes.user.services.SocialLoginService;

@RestController
@RequestMapping("/Login with Facebook")
public class FacebookLoginController {

	@Autowired
	SocialLoginService facebookService;
	
	@GetMapping("/createFacebookAuthorization")
    public String createFacebookAuthorization(){
        return facebookService.createFacebookAuthorizationURL();
    }
	
	@GetMapping("/Loginfacebook")
	public ResponseEntity<ResponseDto> createFacebookAccessToken(@RequestParam("code") String code,HttpServletResponse res){
	    String token =facebookService.createFacebookAccessToken(code);
	    res.setHeader("token",token);
	    
	    ResponseDto response = new ResponseDto();
		response.setMessage("SuccessFully LoggedIn");
		response.setStatus(1);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/*@GetMapping("/getName")
	public String getNameResponse(){
	    return facebookService.getName();
	}*/
}
