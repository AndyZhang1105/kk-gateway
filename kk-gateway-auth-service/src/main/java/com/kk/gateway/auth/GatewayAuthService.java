package com.kk.gateway.auth;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Zal
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class GatewayAuthService {

	public static void main(String[] args) {
		SpringApplication.run(GatewayAuthService.class, args);
	}

}
