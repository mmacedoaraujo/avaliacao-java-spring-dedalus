package com.mmacedoaraujo.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("getAllUsers", route -> route.path("/users")
                        .filters(filter -> filter.rewritePath("users", "/api/v1/users"))
                        .uri("http://localhost:8081")
                )
                .route("addNewUser", route -> route.path("/addNewUser")
                        .filters(filter -> filter.rewritePath("addNewUser", "/api/v1/users/addNewUser"))
                        .uri("http://localhost:8081")
                )
                .build();
    }
}
