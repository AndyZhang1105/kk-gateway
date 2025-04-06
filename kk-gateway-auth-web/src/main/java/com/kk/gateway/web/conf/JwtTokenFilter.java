package com.kk.gateway.web.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kk.gateway.common.JwtResponse;
import com.kk.gateway.common.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 过滤器
 */
public class JwtTokenFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		// 获取请求路径
		String requestURI = request.getRequestURI();

		// 如果请求路径是 /auth，则跳过 JWT 验证
		if ("/auth".equals(requestURI)) {
			chain.doFilter(request, response);
			return;
		}

		// 从请求头中获取 JWT 令牌
		String authHeader = request.getHeader("token");
		String token = null;
		String username = null;

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7); // 去掉 "Bearer "
		} else {
			token = authHeader;
		}

		try {
			// 使用 JwtUtil 解析 JWT 并获取用户名
			username = JwtUtils.getSubjectFromToken(token);
		} catch (Exception e) {
			// 解析失败，可能是令牌无效
			logger.error("Failed to parse JWT token: " + token);
			this.handleJwtException(response, JwtResponse.JWT_RESPONSE_CODE_FAIL, "Invalid JWT token", HttpStatus.FORBIDDEN);
			return;
		}

		if(username == null || username.trim().isEmpty()) {
			this.handleJwtException(response, JwtResponse.JWT_RESPONSE_CODE_FAIL, "Invalid username from JWT token", HttpStatus.FORBIDDEN);
			return;
		}

		// 如果用户名不为空且当前请求没有认证信息
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			// 验证令牌是否有效
			final JwtResponse jwtResponse = JwtUtils.validateToken(token, username);
			if (jwtResponse.getCode() == JwtResponse.JWT_RESPONSE_CODE_SUCCESS) {
				// 加载用户详细信息
				UserDetails userDetails = User.builder().username(username).password("").roles("ADMIN").build();

				// 创建认证信息
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// 设置认证信息到安全上下文
				SecurityContextHolder.getContext().setAuthentication(authToken);
			} else {
				this.handleJwtException(response, jwtResponse.getCode(), jwtResponse.getMsg(), HttpStatus.UNAUTHORIZED);
				return;
			}
		}

		// 继续处理请求
		chain.doFilter(request, response);
	}

	private void handleJwtException(HttpServletResponse response, int code, String message, HttpStatus status) throws IOException {
		response.setStatus(status.value());
		response.setContentType("application/json;charset=UTF-8");

		JwtResponse apiResponse = new JwtResponse(-1, message, "");
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonResponse = objectMapper.writeValueAsString(apiResponse);

		response.getWriter().write(jsonResponse);
	}

}
