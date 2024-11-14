package com.kk.gateway.api.conf;

import com.alibaba.nacos.api.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kk.gateway.common.JwtResponse;
import com.kk.gateway.common.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author Zal
 */
@Configuration
public class GlobalFiltersConfig {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

	@Autowired
	private JwtProperties jwtProperties;

	@Bean
	public GlobalFilter jwtTokenValidator() {
		return (exchange, chain) -> {
			// 获取请求路径
			final String path = exchange.getRequest().getPath().value();

			// 检查请求路径是否在不需要 token 校验的路径列表中
			if (jwtProperties.getSkipPaths().stream().anyMatch(pattern -> PATH_MATCHER.match(pattern, path))) {
				// 如果路径匹配，跳过 token 校验
				return chain.filter(exchange);
			}

			String token = exchange.getRequest().getHeaders().getFirst("token");
			if(StringUtils.isBlank(token)) {
				// 从请求头中获取 Authorization 信息
				String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
				if (authHeader != null && !authHeader.startsWith("Bearer ")) {
					token = authHeader.substring(7); // 去掉 "Bearer "
				}
			}

			// 如果没有 token 或格式不对，返回 401 Unauthorized 并附带自定义消息
			if(StringUtils.isBlank(token)) {
				return createErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "Invalid or missing token");
			}

			try {
				final JwtResponse jwtResponse = JwtUtils.validateToken(token);
				if (jwtResponse.getCode() == JwtResponse.JWT_RESPONSE_CODE_SUCCESS) {  // 你可以根据需要传递用户名
					// 如果 token 有效，继续处理请求
					return chain.filter(exchange);
				} else {
					// 如果 token 无效，返回 401 Unauthorized 并附带自定义消息
					return createErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "Token validation failed");
				}
			} catch (Exception e) {
				// 如果发生异常，返回 500 Internal Server Error 并附带自定义消息
				return createErrorResponse(exchange, HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing the request");
			}
		};
	}

	private Mono<Void> createErrorResponse(ServerWebExchange exchange, HttpStatus status, String message) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(status);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		Map<String, String> body = Map.of("error", status.getReasonPhrase(), "message", message);
		try {
			DataBuffer buffer = response.bufferFactory().wrap(OBJECT_MAPPER.writeValueAsBytes(body));
			return response.writeWith(Mono.just(buffer));
		} catch (Exception e) {
			// 处理序列化异常
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			return response.setComplete();
		}
	}
}