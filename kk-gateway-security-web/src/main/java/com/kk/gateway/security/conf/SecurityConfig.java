package com.kk.gateway.security.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/**
	 * 禁用所有的 CSRF 保护
	 * 允许所有人访问 /public/**, 其他请求需要认证
	 * 允许拥有角色ADMIN才能访问 /admin/**, 其他请求需要认证
	 * 自定义登录页面，登录页面允许所有人访问
	 * 注销页面允许所有人访问
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> {
					RequestMatcher requireCsrfProtectionMatcher = new OrRequestMatcher(
							new AntPathRequestMatcher("/api/**"),
							new AntPathRequestMatcher("/admin/**")
					);
					csrf.ignoringRequestMatchers(requireCsrfProtectionMatcher);  // 忽略某些路径的 CSRF 保护
				})
				.authorizeHttpRequests(authorizeRequests ->
						authorizeRequests
								.requestMatchers("/authenticate").permitAll()
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
								.logoutUrl("/logout")
								.logoutSuccessUrl("/login?logout")  // 注销成功后重定向到登录页面
								.invalidateHttpSession(true) // 使会员失效
								.deleteCookies("JSESSIONID")
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