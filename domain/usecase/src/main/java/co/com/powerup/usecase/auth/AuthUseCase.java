package co.com.powerup.usecase.auth;

import co.com.powerup.model.role.gateways.RoleRepository;
import co.com.powerup.model.user.gateways.UserRepository;
import co.com.powerup.usecase.passwordencoder.PasswordEncoderUseCase;
import co.com.powerup.usecase.tokenservice.TokenUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AuthUseCase implements IAuthUseCase {

    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenUseCase tokenUseCase;
    private final PasswordEncoderUseCase passwordEncoderService;

    @Override
    public Mono<String> login(String email, String password) {
        if (email == null || email.isBlank()) {
            return Mono.error(new IllegalArgumentException("El 'email' no puede ser null o vacío"));
        }
        if (password == null || password.isBlank()) {
            return Mono.error(new IllegalArgumentException("La 'password' no puede ser null o vacía"));
        }

        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Usuario no existente")))
                .flatMap(user ->
                    passwordEncoderService.matches(password, user.getPassword())
                        .flatMap(matches -> {
                            if (!matches) {
                                return Mono.error(new IllegalArgumentException("Contraseña incorrecta"));
                            }
                            return roleRepository.findById(user.getRoleId())
                                    .switchIfEmpty(Mono.error(new IllegalArgumentException("Rol no encontrado para el usuario")))
                                    .flatMap(role -> tokenUseCase.generateToken(user, role));
                        })
                );
    }


}
