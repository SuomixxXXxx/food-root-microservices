package org.chiches.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ServiceTokenDunno {
    @Value("${service.secret}")
    private String serviceSecret;
    @Value("${spring.application.name}")
    private String serviceName;
    @Qualifier("load-balanced")
    private final RestTemplate restTemplate;
    private final TokenHolder tokenHolder;

    public ServiceTokenDunno(
            RestTemplate restTemplate,
            TokenHolder tokenHolder) {
        this.restTemplate = restTemplate;
        this.tokenHolder = tokenHolder;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fetchToken() {
        String url = "http://identity-service/api/v1/auth/service?service=" + serviceName + "&secret=" + serviceSecret;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        tokenHolder.setToken(response.getBody());
        System.out.println("Token fetched: " + response.getBody());
    }
//    @LoadBalanced
//    public RestTemplate loadBalancedRestTemplate() {
//        return new RestTemplate();
//    }
}
