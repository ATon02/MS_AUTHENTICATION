package co.com.powerup.usecase.tokenservice;

import co.com.powerup.model.role.Role;
import co.com.powerup.model.user.User;
import reactor.core.publisher.Mono;

public interface TokenUseCase {
    Mono<String> generateToken(User user, Role role);
}
