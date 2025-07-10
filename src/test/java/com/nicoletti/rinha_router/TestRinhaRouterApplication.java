package com.nicoletti.rinha_router;

import org.springframework.boot.SpringApplication;

public class TestRinhaRouterApplication {

	public static void main(String[] args) {
		SpringApplication.from(RinhaRouterApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
