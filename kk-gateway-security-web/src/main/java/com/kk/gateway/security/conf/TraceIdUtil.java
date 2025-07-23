package com.kk.gateway.security.conf;

import org.slf4j.MDC;
import java.util.UUID;

public class TraceIdUtil {
    public static final String TRACE_ID = "traceId";

    // 生成TraceID并放入MDC
    public static void generateIfAbsent() {
        if (MDC.get(TRACE_ID) == null) {
            MDC.put(TRACE_ID, createTraceId());
        }
    }

    // 创建TraceID (格式: 时间戳-随机数)
    private static String createTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    // 获取当前TraceID
    public static String getCurrentTraceId() {
        return MDC.get(TRACE_ID);
    }

    // 清理TraceID
    public static void clear() {
        MDC.clear();
    }
}