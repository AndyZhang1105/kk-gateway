package com.kk.gateway.conf;

import com.alibaba.cloud.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthGlobalFilter implements GlobalFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String token = exchange.getRequest().getHeaders().getFirst("token");
		if (StringUtils.isBlank(token)) {
			// 鉴权失败
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}
		return chain.filter(exchange);
	}

	private boolean isValidToken(String token) {
		// 这里只是一个简单的示例，实际应用中应使用更复杂的逻辑来验证令牌
		return "Bearer valid-token".equals(token);
	}

}
