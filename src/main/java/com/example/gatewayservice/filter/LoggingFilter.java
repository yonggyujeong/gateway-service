package com.example.gatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() {
        super(Config.class);
    }

    @Data
    public static class Config{
        // 설정 정보
        String baseMessage;
        boolean preLogger;
        boolean postLogger;
    }

    @Override
    public GatewayFilter apply(Config config) {
        //Custom PreFilter
        return (exchange, chain) ->{
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("LoggingFilter baseMessage: -> {}", config.getBaseMessage());

            if(config.isPreLogger()){ //prefilter가 작동되어야 한다면
                log.info("LoggingFilter Start: -> request id -> {}", request.getId());
            }

            // Custom PostFilter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> { // Mono: spring 5 비동기환경에서 단일값 전달하기 위한 클래스
                if(config.isPreLogger()){
                    log.info("Logging Filter end: response code: -> {}", response.getStatusCode());
                }
            }));
        };
    }
}
