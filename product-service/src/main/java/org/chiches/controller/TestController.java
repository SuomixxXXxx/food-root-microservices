package org.chiches.controller;

import org.chiches.feign.OrderServiceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/test-product")
public class TestController {
    private final OrderServiceClient orderServiceClient;

    public TestController(OrderServiceClient orderServiceClient) {
        this.orderServiceClient = orderServiceClient;
    }

    @GetMapping("/api/lol")
    @PostAuthorize("hasAuthority('user::read')")
    public ResponseEntity<String> geteem(Principal principal) {
        ((JwtAuthenticationToken)principal).getTokenAttributes().forEach(
                (k, v) -> System.out.println(k + " " + v)
        );
        return ResponseEntity.ok(null);
    }

    @GetMapping("/fag")
    public ResponseEntity<String> get(Principal principal) {
        return ResponseEntity.ok(orderServiceClient.getFaggot(Long.valueOf(principal.getName())).getBody());
    }
}
