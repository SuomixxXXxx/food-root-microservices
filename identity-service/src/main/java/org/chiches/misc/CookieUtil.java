package org.chiches.misc;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class CookieUtil {
    public static final String JWT_COOKIE_NAME = "jwtToken";
    public static final String REFRESH_COOKIE_NAME = "refreshToken";
    public static final String DOMAIN = "localhost";
    public static final String PATH = "/api";
    public static final boolean SECURE = false;
    private static final boolean HTTP_ONLY_JWT = false;// FIXME: security issue, XSS vulnerability
    private static final boolean HTTP_ONLY_REFRESH = false;
    public Cookie createJWTCookie(String jwt) {
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, "Bearer_" + jwt);
        cookie.setHttpOnly(HTTP_ONLY_JWT);
        cookie.setPath(PATH);
        cookie.setMaxAge(60 * 30);
        cookie.setDomain(DOMAIN);
        cookie.setAttribute("SameSite", "None");
        cookie.setSecure(SECURE);
        return cookie;
    }
    public Cookie createRefreshCookie(String refreshToken) {
        Cookie cookie = new Cookie(REFRESH_COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(HTTP_ONLY_REFRESH);
        cookie.setPath(PATH);
        cookie.setMaxAge(86400);
        cookie.setDomain(DOMAIN);
        cookie.setAttribute("SameSite", "None");
        cookie.setSecure(SECURE);
        return cookie;
    }
    public Cookie deleteJWTCookie() {
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, "");
        cookie.setHttpOnly(HTTP_ONLY_JWT);
        cookie.setPath(PATH);
        cookie.setMaxAge(0);
        cookie.setDomain(DOMAIN);
        cookie.setAttribute("SameSite", "None");
        cookie.setSecure(SECURE);
        return cookie;
    }
    public Cookie deleteRefreshCookie() {
        Cookie cookie = new Cookie(REFRESH_COOKIE_NAME, "");
        cookie.setHttpOnly(HTTP_ONLY_REFRESH);
        cookie.setPath(PATH);
        cookie.setMaxAge(0);
        cookie.setDomain(DOMAIN);
        cookie.setAttribute("SameSite", "None");
        cookie.setSecure(SECURE);
        return cookie;
    }
    public void logout(HttpServletResponse response) {
        Cookie jwtToken = deleteJWTCookie();
        response.addCookie(jwtToken);
        Cookie refreshToken = deleteRefreshCookie();
        response.addCookie(refreshToken);
    }
}
