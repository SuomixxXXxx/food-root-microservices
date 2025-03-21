package org.chiches.service.impl;

import org.chiches.dto.TokenDTO;
import org.chiches.dto.UserDTO;
import org.chiches.entity.AuthorityEntity;
import org.chiches.entity.UserEntity;
import org.chiches.repository.AuthorityRepository;
import org.chiches.repository.UserRepository;
import org.chiches.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Value("${service.secret}")
    private String serviceSecret;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    public AuthenticationServiceImpl(UserRepository userRepository,
                                     AuthenticationManager authenticationManager,
                                     JwtService jwtService,
                                     RefreshTokenService refreshTokenService,
                                     PasswordEncoder passwordEncoder,
                                     AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public TokenDTO authenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

//        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

        return new TokenDTO(token, "refreshToken.getToken()");
    }

    @Override
    public TokenDTO register(UserDTO userDto) {
        if (userRepository.existsByLogin(userDto.getLogin())) {
            throw new IllegalArgumentException("User with login " + userDto.getLogin() + " already exists");
        }
        List<AuthorityEntity> defaultAuthorities = authorityRepository.findByAuthorityContainingIgnoreCase("user");

        UserEntity userEntity = new UserEntity(
                userDto.getLogin(),
                passwordEncoder.encode(userDto.getPassword()),
                userDto.getName(),
                userDto.getSurname(),
                defaultAuthorities
        );
        userRepository.save(userEntity);

        String token = jwtService.generateToken(userEntity);

//        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(userEntity.getLogin());

        return new TokenDTO(token, "refreshToken.getToken()");
    }

    @Override
    public String serviceToken(String service, String secret) {
        if (!serviceSecret.equals(secret)) {
            throw new IllegalArgumentException("Invalid secret");
        }
        List<String> authorities = List.of("service");
        String token = jwtService.generateServiceToken(service, authorities);
        return token;
    }

////    @Override
//    public TokenDTO refresh(String refreshToken) {
//        RefreshTokenEntity refreshTokenEntity = refreshTokenService.verifyExpiration(refreshToken);
//        if (refreshTokenEntity == null) {
//            throw new IllegalArgumentException("Refresh token is expired");
//        }
//        UserEntity userEntity = refreshTokenEntity.getUser();
//        String token = jwtServiceOld.generateToken(userEntity);
//        return new TokenDTO(token, refreshToken);
//    }
}
