package com.kk.gateway.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewaySecurityWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewaySecurityWebApplication.class, args);
	}

}
