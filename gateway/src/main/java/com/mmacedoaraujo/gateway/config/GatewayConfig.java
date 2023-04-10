package com.mmacedoaraujo.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private static final String USERS_API_URI = "lb://users-api";
    public static final String EUREKA_URL = "http://localhost:8083";

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("swagger", route -> route.path("/v3/api-docs")
                        .uri(USERS_API_URI)
                )
                .route("swagger", route -> route.path("/v3/api-docs/**")
                        .filters(filter -> filter.rewritePath("/v3/api-docs/(?<static>.*)", "/v3/api-docs/${static}"))
                        .uri(USERS_API_URI)
                )
                .route("swaggerUI", route -> route.path("/user-api-swagger")
                        .uri(USERS_API_URI)
                )
                .route("swaggerUIStatic", route -> route.path("/swagger-ui/**")
                        .filters(filter -> filter.rewritePath("/swagger-ui/(?<static>.*)", "/swagger-ui/${static}"))
                        .uri(USERS_API_URI)
                )
                .route("discoveryServer", route -> route.path("/eureka")
                        .filters(filter -> filter.rewritePath("/eureka", "/"))
                        .uri(EUREKA_URL)
                )
                .route("discoveryServerStatic", route -> route.path("/eureka/**")
                        .filters(filter -> filter.rewritePath("/eureka/(?<static>.*)", "/eureka/${static}"))
                        .uri(EUREKA_URL)
                )
                .route("returnUsersRegistered", route -> route.path("/api/v1/users")
                        .uri(USERS_API_URI)
                )
                .route("returnUsersRegisteredPageable", route -> route.path("/api/v1/users/paginated")
                        .uri(USERS_API_URI)
                )
                .route("returnUserById", route -> route.path("/api/v1/users/**")
                        .uri(USERS_API_URI)
                )
                .route("addNewUser", route -> route.path("/api/v1/users/addNewUser")
                        .uri(USERS_API_URI)
                )
                .route("updateUserData", route -> route.path("/api/v1/users/updateUser/**")
                        .uri(USERS_API_URI)
                )
                .route("removeUser", route -> route.path("/api/v1/users/deleteUser/**")
                        .uri(USERS_API_URI)
                )
                .build();
    }
}
