package org.chiches.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.chiches.dto.TokenDTO;
import org.chiches.dto.UserDTO;
import org.chiches.misc.CookieUtil;
import org.chiches.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final CookieUtil cookieUtil;
    public AuthController(AuthenticationService authenticationService, CookieUtil cookieUtil) {
        this.authenticationService = authenticationService;
        this.cookieUtil = cookieUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenDTO> register(@RequestBody UserDTO userDto, HttpServletResponse response) {

        return ResponseEntity.ok(authenticationService.register(userDto));
    }

//    @PostMapping("/refresh")
//    public ResponseEntity<TokenDTO> refresh(@RequestParam String refreshToken,HttpServletResponse response) {
//
//        return ResponseEntity.ok(authenticationService.refresh(refreshToken));
//    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDTO> authenticate(@RequestParam String login, @RequestParam String password, HttpServletResponse response) {

        TokenDTO tokenDTO = authenticationService.authenticate(login, password);

        return ResponseEntity.ok(tokenDTO);
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        cookieUtil.logout(response);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/service")
    public ResponseEntity<String> getServiceToken(@RequestParam String service, @RequestParam String secret) {
        return ResponseEntity.ok(authenticationService.serviceToken(service, secret));
    }
}
