package com.bridgelabz.fundoonotes;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= FundoonotesApplication.class)
@SpringBootTest
public class TestServices {
	   private MockMvc mockMvc;

	    @Autowired
	    private WebApplicationContext wac;

	    @Before
	    public void setup() {
	        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

	    }

	   // @Test
	    public void register() throws Exception {
	        mockMvc.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON)
	                .content("{\"userName\":\"pratik\",\"emailId\" :\"pratikpr2@gmail.com\",\"phoneNumber\":\"9999999999\",\" password\":\"pratik@1234\",\"confirmPassword \":\"pratik@1234\"}")
	                .accept(MediaType.APPLICATION_JSON))
	                .andExpect(jsonPath("$.message").value("SuccessFully Registered"))
	                .andExpect(jsonPath("$.status").value(1));
	       
	    }
	   
	   @Test
	    public void login() throws Exception {
	        mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON)
	                .content("{ \"email\" :\"saritaj709@gmail.com\",\"password\":\"mojojojo\"}")
	                .accept(MediaType.APPLICATION_JSON))
	                .andExpect(jsonPath("$.message").value("SuccessFully LoggedIn"))
	                .andExpect(jsonPath("$.status").value(1));
	    }
	    
	    
	
}
