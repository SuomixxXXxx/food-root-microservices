package org.chiches.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final CookieUtil cookieUtil;
    public static final String BEARER_PREFIX = "Bearer_";
    public static final String JWT_COOKIE_NAME = "jwtToken";
    public static final String REFRESH_COOKIE_NAME = "refreshToken";
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService, RefreshTokenService refreshTokenService, CookieUtil cookieUtil) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.refreshTokenService = refreshTokenService;
        this.cookieUtil = cookieUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();

        final Cookie jwtCookie = findCookie(cookies, JWT_COOKIE_NAME);
        final Cookie refreshCookie = findCookie(cookies, REFRESH_COOKIE_NAME);
        final String jwtToken;
        final String login;

        if (jwtCookie == null || jwtCookie.getValue() == null || !jwtCookie.getValue().startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = jwtCookie.getValue().substring(BEARER_PREFIX.length());
        login = jwtService.extractUserName(jwtToken);

        if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(login);
            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else if(jwtService.isTokenExpired(jwtToken)){
                if(refreshCookie == null || refreshCookie.getValue() == null){
                    filterChain.doFilter(request, response);
                    return;
                }
/*                String refreshToken = refreshCookie.getValue();
                RefreshTokenEntity refreshTokenEntity = refreshTokenService.verifyExpiration(refreshToken);
                if(refreshTokenService.findByToken(refreshToken).isPresent() && refreshTokenEntity != null){

                    Cookie refreshTokenCookie = cookieUtil.createRefreshCookie(refreshTokenEntity.getToken());
//                            new Cookie(REFRESH_COOKIE_NAME,refreshTokenEntity.getToken());
//                    refreshTokenCookie.setHttpOnly(true);
//                    refreshTokenCookie.setMaxAge(86400);
//                    refreshTokenCookie.setPath("/api");
                    response.addCookie(refreshTokenCookie);

                    String newJwtToken = jwtService.generateToken(userDetails);
                    Cookie jwtTokenCookie = cookieUtil.createJWTCookie(newJwtToken);
//                            new Cookie(JWT_COOKIE_NAME,BEARER_PREFIX + newJwtToken);
//                    //jwtTokenCookie.setHttpOnly(true);
//                    jwtTokenCookie.setMaxAge(60 * 30);
//                    jwtTokenCookie.setPath("/api");
                    response.addCookie(jwtTokenCookie);

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                }*/
            }
        }
        filterChain.doFilter(request, response);
    }
    private Cookie findCookie(Cookie[] cookies, String name) {
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }
}
