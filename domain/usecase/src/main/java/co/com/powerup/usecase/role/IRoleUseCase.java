package co.com.powerup.usecase.role;

import co.com.powerup.model.role.Role;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IRoleUseCase {

    Mono<Role> saveRole(Role role);
    Flux<Role> findAll();

}
