package com.kk.gateway.web;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Zal
 */
@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
public class GatewayAuthWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayAuthWebApplication.class, args);
	}

}
