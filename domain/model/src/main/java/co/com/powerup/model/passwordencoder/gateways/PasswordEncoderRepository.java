package co.com.powerup.model.passwordencoder.gateways;

import reactor.core.publisher.Mono;

public interface PasswordEncoderRepository {
    Mono<String> encode(String rawPassword);
    Mono<Boolean> matches(String rawPassword, String encodedPassword);
}
