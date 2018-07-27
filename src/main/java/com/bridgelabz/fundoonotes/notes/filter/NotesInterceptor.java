package com.bridgelabz.fundoonotes.notes.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.bridgelabz.fundoonotes.user.repository.UserRepository;
import com.bridgelabz.fundoonotes.user.token.JwtToken;

@Component
public class NotesInterceptor implements HandlerInterceptor {

	@Autowired
	UserRepository repo;
	
	@Autowired
	JwtToken jwt;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object object) throws Exception {
		String token = request.getHeader("token");
		String userId = jwt.getUserId(token);
		//System.out.println(userId);
		if(repo.findById(userId).isPresent()) {
			//System.out.println(jwt.getUserId(token));
			request.setAttribute("token",jwt.getUserId(token));
			
			return true;
			
		}
		return false;
	}
	
}
