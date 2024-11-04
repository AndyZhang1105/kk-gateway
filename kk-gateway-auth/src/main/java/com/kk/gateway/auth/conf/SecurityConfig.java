package com.kk.gateway.auth.conf;

import com.kk.gateway.auth.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/**
	 * 允许所有人访问 /public/**, 其他请求需要认证
	 * 允许拥有角色ADMIN才能访问 /admin/**, 其他请求需要认证
	 * 自定义登录页面，登录页面允许所有人访问
	 * 注销页面允许所有人访问
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(authorizeRequests ->
						authorizeRequests
								.requestMatchers("/public/**").permitAll()  // 允许所有人访问 /public/**
								.requestMatchers("/admin/**").hasRole("ADMIN")  // 只有管理员可以访问 /admin/**
								.anyRequest().authenticated()  // 其他请求需要认证
				)
				.formLogin(formLogin ->
						formLogin
								.loginPage("/login")  // 自定义登录页面
								.defaultSuccessUrl("/index?ok") // 登录成功后重定向到主页
								.permitAll()  // 登录页面允许所有人访问
				)
				.logout(logout ->
						logout
								.logoutSuccessUrl("/login?logout")  // 注销成功后重定向到登录页面
								.permitAll()  // 注销页面允许所有人访问
				);

		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**", "/images/**");  // 忽略静态资源
	}

	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails admin = User.builder().username("admin").password(passwordEncoder().encode("admin")).roles("ADMIN").build();
		UserDetails user = User.builder().username("user").password(passwordEncoder().encode("user")).roles("USER").build();
		return new InMemoryUserDetailsManager(admin, user);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}