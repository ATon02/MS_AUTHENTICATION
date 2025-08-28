package co.com.powerup.usecase.user;

import co.com.powerup.model.role.gateways.RoleRepository;
import co.com.powerup.model.user.User;
import co.com.powerup.model.user.gateways.UserRepository;
import co.com.powerup.usecase.passwordencoder.PasswordEncoderUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase implements IUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoderUseCase passwordEncoderService;

    @Override
    public Mono<User> saveUser(User user) {
        System.out.println("➡️ Ejecutando saveUser de UserUseCase con el user: " + user.toString());
        user.setRoleId(3L);
        return validUser(user)
                .flatMap(userSave -> userRepository.findByEmail(userSave.getEmail())
                        .flatMap(existing -> Mono
                                .<User>error(new IllegalArgumentException("El correo ya está registrado")))
                        .switchIfEmpty(userRepository.saveTransactional(userSave)));
    }

    @Override
    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> saveUserAdmin(User user) {
        return validUser(user)
                .flatMap(userSave -> roleRepository.findById(userSave.getRoleId())
                        .switchIfEmpty(Mono
                                .error(new IllegalArgumentException("El rol con id " + userSave.getRoleId() + " no existe")))
                        .flatMap(roleExist -> userRepository.saveTransactional(userSave)));
    }

    private Mono<User> validUser(User user) {
        return Mono.just(user)
                .flatMap(userSave -> {
                    if (userSave.getName() == null || userSave.getName().isBlank()) {
                        return Mono.error(new IllegalArgumentException("El campo 'name' es obligatorio"));
                    }
                    if (userSave.getLastName() == null || userSave.getLastName().isBlank()) {
                        return Mono.error(new IllegalArgumentException("El campo 'lastName' es obligatorio"));
                    }
                    if (userSave.getEmail() == null || userSave.getEmail().isBlank()) {
                        return Mono.error(new IllegalArgumentException("El campo 'email' es obligatorio"));
                    }
                    if (userSave.getBaseSalary() == null) {
                        return Mono.error(new IllegalArgumentException("El campo 'baseSalary' es obligatorio"));
                    }
                    if (userSave.getBaseSalary() < 0 || userSave.getBaseSalary() > 15_000_000) {
                        return Mono
                                .error(new IllegalArgumentException("El salario base debe estar entre 0 y 15.000.000"));
                    }
                    if (!userSave.getEmail().matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$")) {
                        return Mono.error(new IllegalArgumentException("El email tiene un formato inválido"));
                    }
                    if (userSave.getPassword() == null || userSave.getPassword().isBlank()) {
                        return Mono.error(new IllegalArgumentException("El campo 'password' es obligatorio"));
                    }
                    return passwordEncoderService.encode(userSave.getPassword())
                            .map(encoded -> {
                                userSave.setPassword(encoded);
                                return userSave;
                            });
                });
    }

}
