package org.chiches.feign;

import feign.RequestInterceptor;
import org.chiches.security.TokenHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration {
    private final TokenHolder tokenHolder;

    public FeignClientConfiguration(TokenHolder tokenHolder) {
        this.tokenHolder = tokenHolder;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            String token = tokenHolder.getToken();
            if (token != null) {
                requestTemplate.header("Authorization", "Bearer " + token);
            }
        };
    }

}