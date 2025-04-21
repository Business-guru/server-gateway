package com.example.server_gateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

@Configuration
public class AppConfig {



    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

//    @Bean
//    public WebFilter corsFilter() {
//        return (ServerWebExchange ctx, WebFilterChain chain) -> {
//            ServerHttpRequest request = ctx.getRequest();
//            if (request.getMethod() == HttpMethod.OPTIONS) {
//                ServerHttpResponse response = ctx.getResponse();
//                response.getHeaders().add("Access-Control-Allow-Origin", "*"); // Or your frontend URL
//                response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//                response.getHeaders().add("Access-Control-Allow-Headers", "Authorization, Content-Type");
//                response.getHeaders().add("Access-Control-Allow-Credentials", "true");
//                response.setStatusCode(HttpStatus.OK);
//                return response.setComplete();
//            }
//            return chain.filter(ctx);
//        };
  //  }
}
