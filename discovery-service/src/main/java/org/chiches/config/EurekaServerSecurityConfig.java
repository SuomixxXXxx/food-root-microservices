package org.chiches.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class EurekaServerSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for simplicity in dev
                .csrf(csrf -> csrf.disable())
                // Configure request authorization
                .authorizeHttpRequests(authorize -> authorize
                        // Allow access to Eureka endpoints and actuator
                        .requestMatchers("/eureka/**", "/actuator/**").permitAll()
                        // Any other requests need to be authenticated
                        .anyRequest().authenticated())
                // Optionally enable HTTP Basic authentication if needed
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}