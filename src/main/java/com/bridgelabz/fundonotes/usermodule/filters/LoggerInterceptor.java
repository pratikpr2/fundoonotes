package com.bridgelabz.fundonotes.usermodule.filters;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoggerInterceptor implements HandlerInterceptor {

	Logger log =  org.slf4j.LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object object, Exception arg3)
			throws Exception {
		
		String reqId = UUID.randomUUID().toString();
		request.setAttribute("reqId", reqId);
		log.info("Request is complete "+ request.getRequestURI()+" with Request Id " +reqId);
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object object, ModelAndView model)
			throws Exception {
		String reqId = UUID.randomUUID().toString();
		request.setAttribute("reqId", reqId);
		log.info("Handler execution is complete "+ request.getRequestURI()+" with Request Id " +reqId );
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object object) throws Exception {
		String reqId = UUID.randomUUID().toString();
		request.setAttribute("reqId", reqId);
		log.info("Before Handler execution " + request.getRequestURI()+" with Request Id " +reqId);
		return true;
	}
}
