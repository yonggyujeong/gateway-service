package com.example.gatewayservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    public CustomFilter() {
        super(Config.class);
    }

    public static class Config{
        // 설정 정보
    }

    @Override
    public GatewayFilter apply(Config config) {
        //Custom PreFilter
        return (exchange, chain) ->{
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Custom PRE Filter: request id: -> {}", request.getId());

            // Custom PostFilter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> { // Mono: spring 5 비동기환경에서 단일값 전달하기 위한 클래스
                log.info("Custom PRE Filter: response code: -> {}", response.getStatusCode());
            }));
        };
    }
}
