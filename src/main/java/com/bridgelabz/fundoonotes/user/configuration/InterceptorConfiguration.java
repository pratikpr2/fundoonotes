package com.bridgelabz.fundoonotes.user.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bridgelabz.fundoonotes.notes.filter.NotesInterceptor;
import com.bridgelabz.fundoonotes.user.filters.LoggerInterceptor;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

	@Autowired
	LoggerInterceptor logInterceptor;
	
	/*@Autowired
	NotesInterceptor noteInterceptor;*/
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(logInterceptor).addPathPatterns("/**");
		//registry.addInterceptor(noteInterceptor);
	}
	
}
