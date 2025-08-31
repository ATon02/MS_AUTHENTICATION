package co.com.powerup.usecase.auth;

import co.com.powerup.model.accesstoken.gateways.AccessTokenRepository;
import co.com.powerup.model.passwordencoder.gateways.PasswordEncoderRepository;
import co.com.powerup.model.role.Role;
import co.com.powerup.model.role.gateways.RoleRepository;
import co.com.powerup.model.user.User;
import co.com.powerup.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AccessTokenRepository accessTokenRepository;

    @Mock
    private PasswordEncoderRepository passwordEncoderRepository;

    @InjectMocks
    private AuthUseCase authUseCase;

    private User validUser;
    private Role validRole;

    @BeforeEach
    void setUp() {
        validUser = new User();
        validUser.setEmail("anderson@example.com");
        validUser.setPassword("encoded123");
        validUser.setRoleId(2L);

        validRole = new Role(2L, "cliente", "Cliente del sistema");
    }

    @Test
    void login_whenEmailIsNull_shouldReturnError() {
        StepVerifier.create(authUseCase.login(null, "123"))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("El 'email' no puede ser null o vacío"))
                .verify();
    }

    @Test
    void login_whenEmailIsBlank_shouldReturnError() {
        StepVerifier.create(authUseCase.login("", "123"))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("El 'email' no puede ser null o vacío"))
                .verify();
    }

    @Test
    void login_whenPasswordIsEmpty_shouldReturnError() {
        StepVerifier.create(authUseCase.login("anderson@example.com", ""))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("La 'password' no puede ser null o vacía"))
                .verify();
    }

    @Test
    void login_whenPasswordIsNull_shouldReturnError() {
        StepVerifier.create(authUseCase.login("anderson@example.com", null))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("La 'password' no puede ser null o vacía"))
                .verify();
    }

    @Test
    void login_whenUserNotFound_shouldReturnError() {
        when(userRepository.findByEmail("anderson@example.com")).thenReturn(Mono.empty());

        StepVerifier.create(authUseCase.login("anderson@example.com", "123"))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("Usuario no existente"))
                .verify();
    }

    @Test
    void login_whenPasswordDoesNotMatch_shouldReturnError() {
        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Mono.just(validUser));
        when(passwordEncoderRepository.matches("wrongPass", validUser.getPassword())).thenReturn(Mono.just(false));

        StepVerifier.create(authUseCase.login(validUser.getEmail(), "wrongPass"))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("Contraseña incorrecta"))
                .verify();
    }

    @Test
    void login_whenRoleNotFound_shouldReturnError() {
        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Mono.just(validUser));
        when(passwordEncoderRepository.matches("123", validUser.getPassword())).thenReturn(Mono.just(true));
        when(roleRepository.findById(validUser.getRoleId())).thenReturn(Mono.empty());

        StepVerifier.create(authUseCase.login(validUser.getEmail(), "123"))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("Rol no encontrado para el usuario"))
                .verify();
    }

    @Test
    void login_whenValidCredentials_shouldReturnToken() {
        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Mono.just(validUser));
        when(passwordEncoderRepository.matches("123", validUser.getPassword())).thenReturn(Mono.just(true));
        when(roleRepository.findById(validUser.getRoleId())).thenReturn(Mono.just(validRole));
        when(accessTokenRepository.generateToken(validUser, validRole)).thenReturn(Mono.just("jwt-token-123"));

        StepVerifier.create(authUseCase.login(validUser.getEmail(), "123"))
                .expectNext("jwt-token-123")
                .verifyComplete();

        verify(accessTokenRepository).generateToken(validUser, validRole);
    }
}

