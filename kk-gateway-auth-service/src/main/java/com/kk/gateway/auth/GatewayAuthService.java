package com.kk.gateway.auth;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Zal
 */
@SpringBootApplication
@EnableDubbo
public class GatewayAuthService {

	public static void main(String[] args) {
		SpringApplication.run(GatewayAuthService.class, args);
	}

}
