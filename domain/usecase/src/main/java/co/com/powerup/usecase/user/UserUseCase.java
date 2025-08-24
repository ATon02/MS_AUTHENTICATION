package co.com.powerup.usecase.user;

import co.com.powerup.model.user.User;
import co.com.powerup.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor
public class UserUseCase implements IUserUseCase {

    private final UserRepository userRepository;


    @Override
    public Mono<User> saveUser(User user) {
        System.out.println("➡️ Ejecutando saveUser de UserUseCase con el user: " + user.toString());

        if (user.getName() == null || user.getName().isBlank()) {
            return Mono.error(new IllegalArgumentException("El campo 'name' es obligatorio"));
        }
        if (user.getLastName() == null || user.getLastName().isBlank()) {
            return Mono.error(new IllegalArgumentException("El campo 'lastName' es obligatorio"));
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return Mono.error(new IllegalArgumentException("El campo 'email' es obligatorio"));
        }
        if (user.getBaseSalary() == null) {
            return Mono.error(new IllegalArgumentException("El campo 'baseSalary' es obligatorio"));
        }
        if (user.getBaseSalary() < 0 || user.getBaseSalary() > 15_000_000) {
            return Mono.error(new IllegalArgumentException("El salario base debe estar entre 0 y 15.000.000"));
        }
        if (!user.getEmail().matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$")) {
            return Mono.error(new IllegalArgumentException("El email tiene un formato inválido"));
        }
        user.setRoleId(3L);
        return userRepository.findByEmail(user.getEmail())
            .flatMap(existing -> Mono.<User>error(new IllegalArgumentException("El correo ya está registrado")))
            .switchIfEmpty(userRepository.saveTransactional(user));
    }


    @Override
    public Flux<User> findAll() {
        return userRepository.findAll();
    }




}
