package org.chiches.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth_route", r -> r
                        .path("/api/v1/auth/**")
                        .uri("lb://identity-service")
                )
                .route("order_route", r -> r
                        .path("/api/v1/order/**")
                        .uri("lb://order-service")
                )
                .route("category_route", r -> r
                        .path("/api/v1/categories/**")
                        .uri("lb://product-service")
                )
                .route("dish-item_route", r -> r
                        .path("/api/v1/dishItems/**")
                        .uri("lb://product-service")
                )
                .route("test_product_route", r -> r
                        .path("/api/v1/test-product/**")
                        .uri("lb://product-service")
                )
                .build();
    }
}