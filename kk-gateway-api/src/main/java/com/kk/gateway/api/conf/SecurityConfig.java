package com.kk.gateway.api.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		http
				.csrf(ServerHttpSecurity.CsrfSpec::disable)  // 禁用 CSRF 保护
				.authorizeExchange(exchanges -> exchanges
						.pathMatchers("/public/**", "/auth/**", "/health").permitAll()  // 允许这些路径不需要认证
						.anyExchange().permitAll()  // 其他所有请求都需要认证
				);
		return http.build();
	}

}