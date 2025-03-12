package org.chiches.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

import java.util.Arrays;

public class CookieBearerTokenResolver implements BearerTokenResolver {

    private final String cookieName;
    private final String tokenPrefix;

    public CookieBearerTokenResolver(String cookieName, String tokenPrefix) {
        this.cookieName = cookieName;
        this.tokenPrefix = tokenPrefix;
    }

    @Override
    public String resolve(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> cookieName.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .filter(value -> value != null && value.startsWith(tokenPrefix))
                    .map(value -> value.substring(tokenPrefix.length()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}