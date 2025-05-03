package com.kk.gateway.web.conf;

import com.kk.gateway.common.util.JwtUtils;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	/**
	 * /api/** 和 /admin/** 路径下的请求将不受 CSRF 保护的影响，而其他路径仍然会受到 CSRF 保护。
	 * 允许所有人访问 /public/**, 其他请求需要认证
	 * 允许拥有角色ADMIN才能访问 /admin/**, 其他请求需要认证
	 * 自定义登录页面，登录页面允许所有人访问
	 * 注销页面允许所有人访问
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(authorizeRequests ->
						authorizeRequests
								.requestMatchers("/login", "/register", "/auth/**").permitAll()  // 登录和注册页面无需认证
								.requestMatchers("/public/**").permitAll()  // 允许所有人访问 /public/**
								.requestMatchers("/admin/**").hasRole("ADMIN")  // 只有管理员可以访问 /admin/**
								.anyRequest().authenticated()  // 其他请求需要认证
				)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(new JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public ConfigurableJWTProcessor<SecurityContext> jwtProcessor() {
		return JwtUtils.createJwtProcessor();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**", "/images/**");  // 忽略静态资源
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}