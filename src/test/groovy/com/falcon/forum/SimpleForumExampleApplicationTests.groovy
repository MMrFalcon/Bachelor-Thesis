package com.falcon.forum


import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner)
@SpringBootTest
@ActiveProfiles("test")
class SimpleForumExampleApplicationTests {

	/**
	 * For run this test add environment properties for database password and username
	 */


	@Test
	void contextLoads() {
	}

}
