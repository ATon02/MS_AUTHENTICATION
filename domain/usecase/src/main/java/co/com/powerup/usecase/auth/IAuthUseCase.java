package co.com.powerup.usecase.auth;

import reactor.core.publisher.Mono;

public interface IAuthUseCase {
    Mono<String> login(String email, String password);
}
