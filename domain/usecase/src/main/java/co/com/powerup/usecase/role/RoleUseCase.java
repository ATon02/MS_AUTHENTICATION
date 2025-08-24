package co.com.powerup.usecase.role;

import co.com.powerup.model.role.Role;
import co.com.powerup.model.role.gateways.RoleRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor
public class RoleUseCase implements IRoleUseCase {

    private final RoleRepository roleRepository;

    @Override
    public Mono<Role> saveRole(Role role) {
        System.out.println("➡️ Ejecutando saveRole de  RoleUseCase con el user: " + role.toString());
        if (role.getName() == null || role.getName().isBlank()) {
            return Mono.error(new IllegalArgumentException("El campo 'name' es obligatorio"));
        }
        if (role.getDescription() == null || role.getDescription().isBlank()) {
            return Mono.error(new IllegalArgumentException("El campo 'description' es obligatorio"));
        }
        return roleRepository.saveTransactional(role);
    }

    @Override
    public Flux<Role> findAll() {
        return roleRepository.findAll();
    }



}
