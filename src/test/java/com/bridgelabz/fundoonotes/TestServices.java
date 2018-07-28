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
	                .content("{\"userName\":\"pratik\",\"emailId\":\"pratikpr2@gmail.com\",\"phoneNumber\":\"9999999999\",\" password\":\"pratik@1234\",\"confirmPassword \":\"pratik@1234\"}")
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
	    
		//@Test
		public void activateTest() throws Exception {
			mockMvc.perform(MockMvcRequestBuilders.post("/activateaccount").param("token",
					"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ2ZXJpZnkiLCJpYXQiOjE1MzI0MTMzOTMsInN1YiI6IjViNTVhYzkxMWNhY2U5MmQ3YTUzZGQ2MSJ9.iKwYXsbaFz791y-9QtlrZI_3ew_vhoHxqarZYKbWPVk")
					.content(MediaType.TEXT_PLAIN_VALUE))
			        .andExpect(jsonPath("$.message").value("Account Activated SuccesFully"))
					.andExpect(jsonPath("$.status").value(1));
		}

		//@Test
		public void forgetPasswordTest() throws Exception {
			mockMvc.perform(MockMvcRequestBuilders.post("/forgetpassword").contentType(MediaType.APPLICATION_JSON)
					.content("{\"email\":\"pratikpr2@gmail.com\"}")
					.content(MediaType.TEXT_PLAIN_VALUE))
			        .andExpect(jsonPath("$.message").value("Please Check Mail To Confirm Changing Password"))
					.andExpect(jsonPath("$.status").value(2));
		}

		// @Test
		public void resetPasswordTest() throws Exception {
			mockMvc.perform(MockMvcRequestBuilders.put("/resetpassword").contentType(MediaType.APPLICATION_JSON).param(
					"token",
					"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ2ZXJpZnkiLCJpYXQiOjE1MzI0MTE1MjIsInN1YiI6IjViNTU5YWVlMWNhY2U5MGY1MTQwOGFjOCJ9.EXPuTNFKa6SICBjS7lOAoQjjxrFZ14_YavT2-bu6CQA")
					.content("{ \"password\" :\"pratik@1234\",\"confirmPassword\":\"pratik@1234\"}"))
					.andExpect(jsonPath("$.message").value("Password Changed SuccessFully"))
					.andExpect(jsonPath("$.status").value(3));
		}
		
		
		
		/*********************************************************************************************/
		        /**Tests for NoteController**/
		
		/*//@Test
		public void noteRegisterTest() throws Exception {
			mockMvc.perform(MockMvcRequestBuilders.post("/notes/create").contentType(MediaType.APPLICATION_JSON).header("token","eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ2ZXJpZnkiLCJpYXQiOjE1MzI0MTMzOTMsInN1YiI6IjViNTVhYzkxMWNhY2U5MmQ3YTUzZGQ2MSJ9.iKwYXsbaFz791y-9QtlrZI_3ew_vhoHxqarZYKbWPVk").content(
					"{\"title\":\"note for test\",\"description\":\"note green\",\"userId\":\"5b55ac911cace92d7a53dd61\",\"label\":\"good\",\"color\":\"blue\"}")
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.message").value("Congratulations,your note is successfully created"))
					.andExpect(jsonPath("$.status").value(1));
		}*/
		
		//@Test
			public void noteUpdateTest() throws Exception {
				mockMvc.perform(MockMvcRequestBuilders.put("/notes/update").contentType(MediaType.APPLICATION_JSON).header("token","eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ2ZXJpZnkiLCJpYXQiOjE1MzI0MTMzOTMsInN1YiI6IjViNTVhYzkxMWNhY2U5MmQ3YTUzZGQ2MSJ9.iKwYXsbaFz791y-9QtlrZI_3ew_vhoHxqarZYKbWPVk")
						.content("{\"title\":\"note for mockito test\",\"body\":\"description\",\"color\":\"blue\"}")
						.accept(MediaType.TEXT_PLAIN_VALUE))
						.andExpect(jsonPath("$.message").value("Congratulations,your details are successfully updated"))
						.andExpect(jsonPath("$.status").value(2));
			}
			
			//@Test
					public void noteDeleteTest() throws Exception {
						mockMvc.perform(MockMvcRequestBuilders.post("/notes/delete").param("noteId","").contentType(MediaType.TEXT_PLAIN_VALUE).header("token","eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ2ZXJpZnkiLCJpYXQiOjE1MzI0MTMzOTMsInN1YiI6IjViNTVhYzkxMWNhY2U5MmQ3YTUzZGQ2MSJ9.iKwYXsbaFz791y-9QtlrZI_3ew_vhoHxqarZYKbWPVk")
								.accept(MediaType.TEXT_PLAIN_VALUE))
								.andExpect(jsonPath("$.message").value("Note Trashed"))
								.andExpect(jsonPath("$.status").value(1));
					}
		
		   //	@Test
			public void noteRestoreTest() throws Exception {
				mockMvc.perform(MockMvcRequestBuilders.post("/notes/restore").param("noteId", "").contentType(MediaType.TEXT_PLAIN_VALUE).header("token","eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ2ZXJpZnkiLCJpYXQiOjE1MzI0MTMzOTMsInN1YiI6IjViNTVhYzkxMWNhY2U5MmQ3YTUzZGQ2MSJ9.iKwYXsbaFz791y-9QtlrZI_3ew_vhoHxqarZYKbWPVk")
						.accept(MediaType.TEXT_PLAIN_VALUE))
						.andExpect(jsonPath("$.message").value("Note Restored"))
						.andExpect(jsonPath("$.status").value(4));
			}
			
			//@Test
			public void noteAddReminderTest() throws Exception {
				mockMvc.perform(MockMvcRequestBuilders.post("/notes/addreminder").contentType(MediaType.APPLICATION_JSON).header("token","eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ2ZXJpZnkiLCJpYXQiOjE1MzI0MTMzOTMsInN1YiI6IjViNTVhYzkxMWNhY2U5MmQ3YTUzZGQ2MSJ9.iKwYXsbaFz791y-9QtlrZI_3ew_vhoHxqarZYKbWPVk")
						.content("{\"day\":\"6\" , \"month\":\"11\",\"year\":\"2020\", \"hours\":\"12\",\"minutes\":\"20\",\"seconds\":\"34\"}")
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(jsonPath("$.message").value("Reminder Added"))
						.andExpect(jsonPath("$.status").value(1));
			}
			
			
			
			//@Test
			public void noteRemoveReminderTest() throws Exception {
				mockMvc.perform(MockMvcRequestBuilders.post("/notes/removereminder")
						.param("noteId", "")
						.contentType(MediaType.TEXT_PLAIN_VALUE)
						.header("token","eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ2ZXJpZnkiLCJpYXQiOjE1MzI0MTMzOTMsInN1YiI6IjViNTVhYzkxMWNhY2U5MmQ3YTUzZGQ2MSJ9.iKwYXsbaFz791y-9QtlrZI_3ew_vhoHxqarZYKbWPVk")
						.accept(MediaType.TEXT_PLAIN_VALUE))
						.andExpect(jsonPath("$.message").value("Reminder Removed"))
						.andExpect(jsonPath("$.status").value(1));
					}

			//@Test
			public void noteDeleteForeverTest() throws Exception {
				mockMvc.perform(MockMvcRequestBuilders.delete("/notes/deleteForever")
						.param("noteId", "")
						.contentType(MediaType.TEXT_PLAIN_VALUE).header("token","eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ2ZXJpZnkiLCJpYXQiOjE1MzI0MTMzOTMsInN1YiI6IjViNTVhYzkxMWNhY2U5MmQ3YTUzZGQ2MSJ9.iKwYXsbaFz791y-9QtlrZI_3ew_vhoHxqarZYKbWPVk")
						.accept(MediaType.TEXT_PLAIN_VALUE))
						.andExpect(jsonPath("$.message").value("Deleted Permanently"))
						.andExpect(jsonPath("$.status").value(1));
			}

	
}
