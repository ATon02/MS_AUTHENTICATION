package co.com.powerup.model.role.gateways;

import co.com.powerup.model.role.Role;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoleRepository {

    Mono<Role> saveTransactional(Role role); 
    Mono<Role> findById(Long id); 
    Flux<Role> findAll();
}
