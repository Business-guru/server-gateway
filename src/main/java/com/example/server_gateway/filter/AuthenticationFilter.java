package com.example.server_gateway.filter;

import jakarta.ws.rs.NotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public AuthenticationFilter()
    {
        super(Config.class);
    }

    public Mono<String> callAuthenticationService(String authHeader) {
        return webClientBuilder.build()
                .get()
                .uri("http://AUTHENTICATION-SERVICE/api/v1/demo-controller")
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), // Check if status is NOT 2xx
                        clientResponse -> Mono.error(new RuntimeException("Authentication Failed: " + clientResponse.statusCode()))
                )
                .bodyToMono(String.class);
    }
    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
      //      try {
                // Set headers with Authorization token
                if (validator.isSecured.test(exchange.getRequest())) {
                    String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
//                    HttpHeaders headers = new HttpHeaders();
//                    headers.set("Authorization", authHeader);
//
//                    // Create request entity
//                    HttpEntity<String> requestEntity = new HttpEntity<>(headers);
//
//                    // Make GET request
//                    ResponseEntity<String> response = restTemplate.exchange("http://AUTHENTICATION-SERVICE/api/v1/demo-controller", HttpMethod.GET, requestEntity, String.class);
//
//                    // Check response status
//                    if (!response.getStatusCode().is2xxSuccessful()) {
//                        throw new NotAuthorizedException("Not authorized:" + response.getStatusCode());
//                    }
//                }
//            } catch (HttpClientErrorException e) {
//                throw new RuntimeException("API call failed: " + e.getMessage());
                    return callAuthenticationService(authHeader)
                            .doOnNext(response -> System.out.println("Response: " + response))
                            .doOnError(error -> System.err.println("Error: " + error.getMessage()))
                            .flatMap(response -> chain.filter(exchange))  // Proceed only if successful
                            .onErrorResume(error -> Mono.error(new RuntimeException("Authentication Failed: " + error.getMessage()))); // Throw error if authentication fails
          }
           return chain.filter(exchange);
        }));
    }

    public static class Config{

    }

}
