package co.com.powerup.usecase.passwordencoder;

import reactor.core.publisher.Mono;

public interface PasswordEncoderUseCase {
    Mono<String> encode(String rawPassword);
    Mono<Boolean> matches(String rawPassword, String encodedPassword);
}
