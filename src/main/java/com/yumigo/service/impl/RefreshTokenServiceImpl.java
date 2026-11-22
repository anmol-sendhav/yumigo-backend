package com.yumigo.service.impl;

import com.yumigo.config.JwtProperties;
import com.yumigo.entity.RefreshToken;
import com.yumigo.entity.User;
import com.yumigo.enums.UserStatus;
import com.yumigo.exception.ForbiddenException;
import com.yumigo.exception.UnauthorizedException;
import com.yumigo.repository.RefreshTokenRepository;
import com.yumigo.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties props;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public RefreshToken create(User user) {
        byte[] bytes = new byte[48];
        secureRandom.nextBytes(bytes);
        String tokenValue = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(tokenValue);
        token.setExpiresAt(LocalDateTime.now().plusDays(props.refreshTokenExpiryDays()));
        token.setRevoked(false);
        return refreshTokenRepository.save(token);
    }

    @Transactional(noRollbackFor = UnauthorizedException.class)
    public RefreshToken rotate(String tokenValue) {
        RefreshToken old = refreshTokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (old.isRevoked()) {
            refreshTokenRepository.revokeAllByUserId(old.getUser().getId());
            throw new UnauthorizedException("Invalid refresh token");
        }
        if (old.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedException("Refresh token expired");
        }

        User user = old.getUser();
        if (user.getStatus() == UserStatus.BLOCKED || user.getStatus() == UserStatus.DELETED) {
            throw new ForbiddenException("This account is not allowed to sign in");
        }

        old.setRevoked(true);
        return create(user);
    }

    @Transactional
    public void revoke(String tokenValue, Long userId) {
        refreshTokenRepository.findByToken(tokenValue)
                .filter(t -> t.getUser().getId().equals(userId))
                .ifPresent(t -> t.setRevoked(true));
    }
}