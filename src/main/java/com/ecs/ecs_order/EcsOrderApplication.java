package com.ecs.ecs_order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EcsOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcsOrderApplication.class, args);
	}

}
