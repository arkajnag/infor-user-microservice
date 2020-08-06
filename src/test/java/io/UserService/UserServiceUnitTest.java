package io.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.UserService.model.User;
import io.UserService.model.UserDTO;


@SpringBootTest(classes = UserServiceApplication.class)
public class UserServiceUnitTest extends AbstractTestNGSpringContextTests{
	
	private MockMvc mockMvc;
	private Logger logger = LoggerFactory.getLogger(UserServiceUnitTest.class);
	private ObjectMapper mapper=new ObjectMapper();
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@BeforeClass
	public void setUpMockMVC(){
		mockMvc= MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@DataProvider
	public Object[][] getNewUserData(){
		return new Object[][]{
				{101,"TestUser1"},
				{102,"TestUser2"}
		};
	}
	
	@Test(priority = 1, description = "Verify New User Information added by implementing POST Method:addNewUserInformation", dataProvider = "getNewUserData")
	public void tcAddNewUserInformation(Integer userid,String userName) throws Exception {	
		logger.info(Thread.currentThread().getStackTrace()[2].getMethodName()+" has started its Execution");
		try {
			User requestUserObject=new User();
			requestUserObject.setUserid(userid);
			requestUserObject.setUsername(userName);
			String requestUserJsonString=mapper.writeValueAsString(requestUserObject);
			MvcResult mvcResult = mockMvc.perform(post("/rest/user/new/user").content(requestUserJsonString).contentType(MediaType.APPLICATION_JSON))
					.andExpect(request().asyncStarted())
					.andReturn();
			mockMvc.perform(asyncDispatch(mvcResult))
					.andExpect(status().isCreated())
					.andExpect(content().contentType("application/json")).andReturn().getResponse().getContentAsString();
		}finally {
			logger.info(Thread.currentThread().getStackTrace()[2].getMethodName()+" has ended its Execution");
		}
	}
	
	@Test(priority = 2, description = "Verify GET all User Information Service by implementing GET Method:getAllUserInformation", 
			dependsOnMethods = "tcAddNewUserInformation")
	public void tcGetAllUserInformation() throws Exception {
		logger.info(Thread.currentThread().getStackTrace()[2].getMethodName()+" has started its Execution");
		try {
				MvcResult mvcResult=mockMvc.perform(get("/rest/user/all"))
						.andExpect(request().asyncStarted())
						.andReturn();
				String jsonResponseString=mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
				UserDTO userWrapperResponseObj=mapper.readValue(jsonResponseString, UserDTO.class);
				Assert.assertEquals(userWrapperResponseObj.getUsers().size(), 2, "Number of Users are not as expected.Please re-visit your test data.");
		}finally {
			logger.info(Thread.currentThread().getStackTrace()[2].getMethodName()+" has ended its Execution");
		}
	}
}
