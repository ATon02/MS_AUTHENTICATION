package co.com.powerup.jwttokenservice;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import co.com.powerup.model.role.Role;
import co.com.powerup.model.user.User;
import co.com.powerup.usecase.tokenservice.TokenUseCase;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import reactor.core.publisher.Mono;

@Component
public class JwtTokenService implements TokenUseCase {

    @Value("${SPRING_SECRET_KEY}")
    private String secretKey;
    private final long expirationMs = 3600000;

    @SuppressWarnings("deprecation")
    @Override
    public Mono<String> generateToken(User user, Role role) {
        return Mono.just(Jwts.builder()
                .setSubject(user.getEmail())
                .claim("roleId", role.getId())
                .claim("role", role.getName())
                .claim("name", user.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact());
    }
}