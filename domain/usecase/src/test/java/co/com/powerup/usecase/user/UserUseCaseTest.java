package co.com.powerup.usecase.user;

import co.com.powerup.model.role.Role;
import co.com.powerup.model.role.gateways.RoleRepository;
import co.com.powerup.model.user.User;
import co.com.powerup.model.user.gateways.UserRepository;
import co.com.powerup.usecase.passwordencoder.PasswordEncoderUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoderUseCase passwordEncoderService;

    @InjectMocks
    private UserUseCase userUseCase;

    private User validUser;

    @BeforeEach
    void setUp() {
        validUser = new User();
        validUser.setName("Anderson");
        validUser.setLastName("Tonusco");
        validUser.setEmail("anderson@example.com");
        validUser.setBaseSalary(5_000.0);
        validUser.setRoleId(3L);
        validUser.setPassword("123456");
    }

    @Test
    void saveUser_whenValidUser_shouldSaveSuccessfully() {
        when(passwordEncoderService.encode(validUser.getPassword())).thenReturn(Mono.just("encoded123"));
        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Mono.empty());
        when(userRepository.saveTransactional(any(User.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));
        StepVerifier.create(userUseCase.saveUser(validUser))
                .expectNextMatches(user -> user.getEmail().equals("anderson@example.com") &&
                        user.getPassword().equals("encoded123"))
                .verifyComplete();
        verify(passwordEncoderService).encode("123456");
        verify(userRepository).findByEmail(validUser.getEmail());
        verify(userRepository).saveTransactional(any(User.class));
    }

    @Test
    void saveUser_whenEmailAlreadyExists_shouldReturnError() {
        when(passwordEncoderService.encode(any())).thenReturn(Mono.just("encoded123"));
        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Mono.just(validUser));
        StepVerifier.create(userUseCase.saveUser(validUser))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("El correo ya está registrado"))
                .verify();
        verify(userRepository).findByEmail(validUser.getEmail());
        verify(userRepository, never()).saveTransactional(any());
    }

    @Test
    void saveUserAdmin_whenRoleExists_shouldSaveSuccessfully() {
        when(passwordEncoderService.encode(validUser.getPassword())).thenReturn(Mono.just("encoded123"));
        when(roleRepository.findById(validUser.getRoleId()))
                .thenReturn(Mono.just(new Role(3L, "Admin", "Administrador")));
        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Mono.empty());
        when(userRepository.saveTransactional(any(User.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));
        StepVerifier.create(userUseCase.saveUserAdmin(validUser))
                .expectNextMatches(user -> user.getPassword().equals("encoded123"))
                .verifyComplete();
        verify(roleRepository).findById(3L);
        verify(userRepository).findByEmail(validUser.getEmail());
        verify(userRepository).saveTransactional(any(User.class));
    }

    @Test
    void saveUserAdmin_whenRoleDoesNotExist_shouldReturnError() {
        when(passwordEncoderService.encode(any())).thenReturn(Mono.just("encoded123"));
        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Mono.empty());
        when(roleRepository.findById(validUser.getRoleId())).thenReturn(Mono.empty());
        StepVerifier.create(userUseCase.saveUserAdmin(validUser))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("El rol con id 3 no existe"))
                .verify();
        verify(roleRepository).findById(3L);
        verify(userRepository, never()).saveTransactional(any());
    }


    @Test
    void findAll_shouldReturnUsers() {
        when(userRepository.findAll()).thenReturn(Flux.just(validUser));
        StepVerifier.create(userUseCase.findAll())
                .expectNext(validUser)
                .verifyComplete();
        verify(userRepository).findAll();
    }

    @Test
    void saveUser_whenNameIsNull_shouldReturnError() {
        validUser.setName(null);
        StepVerifier.create(userUseCase.saveUser(validUser))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("El campo 'name' es obligatorio"))
                .verify();
        verify(userRepository, never()).saveTransactional(any());
    }

    @Test
    void saveUser_whenNameIsBlank_shouldReturnError() {
        validUser.setName("");
        StepVerifier.create(userUseCase.saveUser(validUser))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("El campo 'name' es obligatorio"))
                .verify();
        verify(userRepository, never()).saveTransactional(any());
    }

    @Test
    void saveUser_whenLastNameIsBlank_shouldReturnError() {
        validUser.setLastName("");
        StepVerifier.create(userUseCase.saveUser(validUser))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("El campo 'lastName' es obligatorio"))
                .verify();
        verify(userRepository, never()).saveTransactional(any());
    }

    @Test
    void saveUser_whenLastNameIsNull_shouldReturnError() {
        validUser.setLastName(null);
        StepVerifier.create(userUseCase.saveUser(validUser))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("El campo 'lastName' es obligatorio"))
                .verify();
        verify(userRepository, never()).saveTransactional(any());
    }

    @Test
    void saveUser_whenEmailIsInvalid_shouldReturnError() {
        validUser.setEmail("correo-invalido");
        StepVerifier.create(userUseCase.saveUser(validUser))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("El email tiene un formato inválido"))
                .verify();
        verify(userRepository, never()).saveTransactional(any());
    }

    @Test
    void saveUser_whenEmailIsBlank_shouldReturnError() {
        validUser.setEmail(""); 
        StepVerifier.create(userUseCase.saveUser(validUser))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("El campo 'email' es obligatorio"))
                .verify();
        verify(userRepository, never()).saveTransactional(any());
    }

    @Test
    void saveUser_whenEmailIsNull_shouldReturnError() {
        validUser.setEmail(null); 
        StepVerifier.create(userUseCase.saveUser(validUser))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("El campo 'email' es obligatorio"))
                .verify();
        verify(userRepository, never()).saveTransactional(any());
    }

    @Test
    void saveUser_whenBaseSalaryIsNegative_shouldReturnError() {
        validUser.setBaseSalary(-1.0); 
        StepVerifier.create(userUseCase.saveUser(validUser))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("El salario base debe estar entre 0 y 15.000.000"))
                .verify();
        verify(userRepository, never()).saveTransactional(any());
    }


    @Test
    void saveUser_whenBaseSalaryTooHigh_shouldReturnError() {
        validUser.setBaseSalary(20_000_000.0);
        StepVerifier.create(userUseCase.saveUser(validUser))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("El salario base debe estar entre 0 y 15.000.000"))
                .verify();
        verify(userRepository, never()).saveTransactional(any());
    }

     @Test
    void saveUser_whenBaseSalaryIsNull_shouldReturnError() {
        validUser.setBaseSalary(null);
        StepVerifier.create(userUseCase.saveUser(validUser))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("El campo 'baseSalary' es obligatorio"))
                .verify();
        verify(userRepository, never()).saveTransactional(any());
    }

    @Test
    void saveUser_whenPasswordIsNull_shouldReturnError() {
        validUser.setPassword(null);
        StepVerifier.create(userUseCase.saveUser(validUser))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("El campo 'password' es obligatorio"))
                .verify();
        verify(userRepository, never()).saveTransactional(any());
    }

    @Test
    void saveUser_whenPasswordIsBlank_shouldReturnError() {
        validUser.setPassword("");
        StepVerifier.create(userUseCase.saveUser(validUser))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("El campo 'password' es obligatorio"))
                .verify();
        verify(userRepository, never()).saveTransactional(any());
    }
}
