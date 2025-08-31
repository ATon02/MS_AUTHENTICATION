package co.com.powerup.usecase.auth;

import co.com.powerup.model.accesstoken.gateways.AccessTokenRepository;
import co.com.powerup.model.passwordencoder.gateways.PasswordEncoderRepository;
import co.com.powerup.model.role.gateways.RoleRepository;
import co.com.powerup.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AuthUseCase implements IAuthUseCase {

    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final PasswordEncoderRepository passwordEncoderRepository;

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
                    passwordEncoderRepository.matches(password, user.getPassword())
                        .flatMap(matches -> {
                            if (!matches) {
                                return Mono.error(new IllegalArgumentException("Contraseña incorrecta"));
                            }
                            return roleRepository.findById(user.getRoleId())
                                    .switchIfEmpty(Mono.error(new IllegalArgumentException("Rol no encontrado para el usuario")))
                                    .flatMap(role -> accessTokenRepository.generateToken(user, role));
                        })
                );
    }


}
