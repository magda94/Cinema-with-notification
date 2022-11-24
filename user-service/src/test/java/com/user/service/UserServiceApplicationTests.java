package com.user.service;

import com.user.service.container.PostgresqlContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext //for testcontainers, because it's killed after test and it's problem with connection
class UserServiceApplicationTests extends PostgresqlContainer {

	@Test
	void contextLoads() {
	}

}
