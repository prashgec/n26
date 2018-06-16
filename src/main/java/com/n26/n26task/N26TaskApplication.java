package com.n26.n26task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.n26")
public class N26TaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(N26TaskApplication.class, args);
	}
}
