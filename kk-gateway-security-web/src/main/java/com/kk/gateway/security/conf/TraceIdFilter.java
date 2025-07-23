package com.kk.gateway.security.conf;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@Order(1) // 确保最先执行
@Slf4j
public class TraceIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            // 生成TraceID
            TraceIdUtil.generateIfAbsent();

            log.info("TraceId: {}", TraceIdUtil.getCurrentTraceId());

            // 继续处理请求
            chain.doFilter(request, response);
        } finally {
            // 清理线程上下文
            TraceIdUtil.clear();
        }
    }
}