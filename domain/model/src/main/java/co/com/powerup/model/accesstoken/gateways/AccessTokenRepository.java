package co.com.powerup.model.accesstoken.gateways;

import co.com.powerup.model.role.Role;
import co.com.powerup.model.user.User;
import reactor.core.publisher.Mono;

public interface AccessTokenRepository {
        Mono<String> generateToken(User user, Role role);
}
