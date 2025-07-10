package com.nicoletti.rinharouter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class RinhaRouterApplicationTests {

	@Test
	void contextLoads() {
	}

}
