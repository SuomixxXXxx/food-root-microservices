package org.chiches.service.impl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.chiches.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.KeyPair;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final KeyPair keyPair;

    public JwtService(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof UserEntity customUserDetails) {
            claims.put("role", customUserDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toArray()
            );
        }
        return generateToken(claims, userDetails);
    }
    public String generateServiceToken(String serviceName, List<String> authorities) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", authorities.toArray());
        return Jwts.builder()
                .claims(claims)
                .subject(serviceName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (1000L * 60L * 3000L))) // 30 minutes
                .signWith(keyPair.getPrivate())
                .compact();
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (1000L * 60L * 30L))) // 30 minutes
                .signWith(keyPair.getPrivate())
                .compact();
    }
}