package io.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@SpringBootTest
class UserServiceApplicationTests extends AbstractTestNGSpringContextTests{

	private Logger logger = LoggerFactory.getLogger(UserServiceApplicationTests.class);
	@Test
	void contextLoads() {
		logger.info("Sample Unit Test Case for Spring Boot");
	}

}
