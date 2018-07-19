package com.bridgelabz.fundoonotes;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.fundoonotes.user.model.LoginDTO;
import com.bridgelabz.fundoonotes.user.services.UserService;

public class TestServices {
	   private MockMvc mockMvc;

	    @Autowired
	    private WebApplicationContext wac;

	    @Before
	    public void setup() {
	        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

	    }

	    @Test
	    public void register() throws Exception {
	        mockMvc.perform(MockMvcRequestBuilders.post("/save").contentType(MediaType.APPLICATION_JSON)
	                .content("{\"userName\":\"Shruti\",\"email\" :\"shrutilaxetti@gmail.com\",\"contactNum\":\"9449374263\",\" password\":\"Shru@1234\",\"confirmpassword \":\"Shru@1234\"}")
	                .accept(MediaType.APPLICATION_JSON))
	                //.andExpect(jsonPath("$.userName").exists())
	                //.andExpect(jsonPath("$.email").exists())
	                //.andExpect(jsonPath("$.contactNum").exists())
	                //.andExpect(jsonPath("$.password").exists())
	                //.andExpect(jsonPath("$.confirmpassword").exists())
	                .andExpect(jsonPath("$.message").value("Registration Successfull!!"))
	                .andExpect(jsonPath("$.status").value(202));
	       
	    }
	   
	    //@Test
	    public void login() throws Exception {
	        mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON)
	                .content("{ \"email\" :\"shrutilaxetti@gmail.com\",\"password\":\"Shru@1234\"}")
	                .accept(MediaType.APPLICATION_JSON))
	                //.andExpect(jsonPath("$.email").exists())
	                //.andExpect(jsonPath("$.password").exists())
	                .andExpect(jsonPath("$.message").value("Login Successfull"))
	                .andExpect(jsonPath("$.status").value(201));
	    }
	
}
