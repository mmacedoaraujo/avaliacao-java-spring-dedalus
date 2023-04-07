package com.mmacedoaraujo.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private static final String USERS_API_URI = "http://localhost:8081";

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("returnUsersRegistered", route -> route.path("/users")
                        .filters(filter -> filter.rewritePath("/users", "/api/v1/users"))
                        .uri(USERS_API_URI)
                )
                .route("returnUsersRegisteredPageable", route -> route.path("/usersPaginated")
                        .filters(filter -> filter.rewritePath("/usersPaginated", "/api/v1/users/paginated"))
                        .uri(USERS_API_URI)
                )
                .route("returnUserById", route -> route.path("/users/**")
                        .filters(filter -> filter.rewritePath("/users/(?<id>.*)", "/api/v1/users/${id}"))
                        .uri(USERS_API_URI)
                )
                .route("addNewUser", route -> route.path("/addNewUser")
                        .filters(filter -> filter.rewritePath("/addNewUser", "/api/v1/users/addNewUser"))
                        .uri(USERS_API_URI)
                )
                .route("updateUserData", route -> route.path("/updateUser/**")
                        .filters(filter -> filter.rewritePath("/updateUser/(?<id>.*)", "/api/v1/users/updateUser/${id}"))
                        .uri(USERS_API_URI)
                )
                .build();
    }
}
