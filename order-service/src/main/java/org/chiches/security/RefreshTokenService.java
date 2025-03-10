package org.chiches.security;

import org.chiches.entity.RefreshTokenEntity;
import org.chiches.repository.RefreshTokenRepository;
import org.chiches.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    //@Value("${jwt.refreshTokenExpiration}")
    private long expirationTime = 86400000L;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshTokenEntity createRefreshToken(String login) {
        Optional<RefreshTokenEntity>  refreshTokenEntity= refreshTokenRepository.findByUserLogin(login);

        refreshTokenEntity.ifPresent(refreshTokenRepository::delete);

        RefreshTokenEntity refreshToken = new RefreshTokenEntity(
                userRepository.findByLogin(login).get(),
                UUID.randomUUID().toString(),
                Instant.now().plusMillis(expirationTime)

        );

        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }
    @Transactional
    public RefreshTokenEntity verifyExpiration(String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh Token is not in the database"));

        if (refreshTokenEntity.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshTokenEntity);
            return null;
        }
        refreshTokenEntity.setExpiryDate(Instant.now().plusMillis(expirationTime));
        refreshTokenRepository.save(refreshTokenEntity);
        return refreshTokenEntity;
    }

    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUserId(userId);
    }

    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
}
