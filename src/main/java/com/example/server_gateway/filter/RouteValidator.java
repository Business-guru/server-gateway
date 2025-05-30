package com.example.server_gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints= List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/authenticate",
            "/api/v1/mentorship/mentor/getAllMentors",
            "/api/v1/mentorship/mentor/searchMentors",
            "/api/v1/mentorship/blogs/getAllBlogs",
            "/service/api/v1/novels/covers",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured=
            request->openApiEndpoints.stream().noneMatch(uri->request.getURI().getPath().contains(uri));
}
