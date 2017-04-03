package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.service.STConnector;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		System.out.println("\n\n"+System.getProperty("portName")+"\n");
		SpringApplication.run(ServerApplication.class, args);
		
		
	}
}
