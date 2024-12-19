package com.tumi.haul.security.jwt;

import com.tumi.haul.model.primitives.Email;
import com.tumi.haul.model.primitives.PhoneNumber;
import com.tumi.haul.model.refresh.RefreshToken;
import com.tumi.haul.model.user.BaseUser;
import com.tumi.haul.model.user.Client;
import com.tumi.haul.model.user.User;
import com.tumi.haul.repository.ClientRepository;
import com.tumi.haul.repository.RefreshTokenRepository;
import com.tumi.haul.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${token.expiration-time-ms}")
    private long EXPIRATION_TIME_MS ;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, ClientRepository clientRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
    }

    public RefreshToken generateRefreshToken(String username) {
        BaseUser baseUser;

        // Determine whether the username is an email or phone number
        if (username.contains("@")) {
            // Search by email in User and Client repositories
            baseUser = userRepository.findByEmail(new Email(username))
                    .map(u -> (BaseUser) u)  // Cast User or Client to BaseUser
                    .orElseGet(() -> clientRepository.findByEmail(new Email(username))
                            .map(c -> (BaseUser) c)
                            .orElseThrow(() -> new RuntimeException("User or Client not found with email")));
        } else {
            // Search by phone number in User and Client repositories
            baseUser = userRepository.findByPhoneNumber(new PhoneNumber(username))
                    .map(u -> (BaseUser) u)
                    .orElseGet(() -> clientRepository.findByPhoneNumber(new PhoneNumber(username))
                            .map(c -> (BaseUser) c)
                            .orElseThrow(() -> new RuntimeException("User or Client not found with phone number")));
        }

        // Create the RefreshToken and set either user or client based on the type of baseUser
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expirationDate(Instant.now().plusMillis(EXPIRATION_TIME_MS))
                .build();

        // Check and assign the correct type
        if (baseUser instanceof User) {
            refreshToken.setUser((User) baseUser);  // Associate User
        } else if (baseUser instanceof Client) {
            refreshToken.setClient((Client) baseUser);  // Associate Client
        }

        String userId = (baseUser instanceof User) ? ((User) baseUser).getId() : ((Client) baseUser).getId();
        return refreshTokenRepository.save(refreshToken);
    }



    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyRTExpiration(RefreshToken refreshToken) {
        logger.info("VERIFYING REFRESH TOKEN...");
        if (refreshToken.getExpirationDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            logger.warn("Expired refresh token deleted: {}", refreshToken.getToken());
            throw new RuntimeException(refreshToken.getToken() + " is expired");
        }
        return refreshToken;
    }
    public void logout(String tokenId) {
        logger.info("ID PASSED: {}", tokenId);
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findById(tokenId);
        if (refreshTokenOptional.isPresent()) {
            refreshTokenRepository.delete(refreshTokenOptional.get());
            logger.info("User logged out and refresh token invalidated: {}", refreshTokenOptional);
        } else {
            throw new RuntimeException("Token not found");
        }
    }

}
