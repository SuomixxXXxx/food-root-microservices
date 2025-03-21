package org.chiches.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class TestController {
    @GetMapping("/api/lol")
    @PostAuthorize("hasAuthority('user::read')")
    public ResponseEntity<String> geteem(Principal principal) {
        ((JwtAuthenticationToken)principal).getTokenAttributes().forEach(
                (k, v) -> System.out.println(k + " " + v)
        );
        return ResponseEntity.ok(null);
    }
}
