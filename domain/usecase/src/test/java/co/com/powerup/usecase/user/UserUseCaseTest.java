package co.com.powerup.usecase.user;

import co.com.powerup.model.user.User;
import co.com.powerup.model.user.gateways.UserRepository;
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

    @InjectMocks
    private UserUseCase userUseCase;

    private User validUser;

    @BeforeEach
    void setUp() {
        validUser = new User();
        validUser.setName("Anderson");
        validUser.setLastName("Tonusco");
        validUser.setEmail("anderson@example.com");
        validUser.setBaseSalary(5000.0);
        validUser.setRoleId(3L);
    }

    @Test
    void saveUser_whenValidUser_shouldSaveSuccessfully() {
        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Mono.empty());
        when(userRepository.saveTransactional(any(User.class))).thenReturn(Mono.just(validUser));

        StepVerifier.create(userUseCase.saveUser(validUser))
                .expectNextMatches(user -> user.getEmail().equals("anderson@example.com"))
                .verifyComplete();

        verify(userRepository, times(1)).findByEmail(validUser.getEmail());
        verify(userRepository, times(1)).saveTransactional(validUser);
    }



    @Test
    void saveUser_whenNameIsNull_shouldReturnError() {
         User user = new User();
        user.setName(null);
        user.setLastName("Tonusco");
        user.setEmail("anderson@example.com");
        user.setBaseSalary(5000.0);

        StepVerifier.create(userUseCase.saveUser(user))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El campo 'name' es obligatorio"))
                .verify();

        verify(userRepository, never()).saveTransactional(any());
}

    @Test
    void saveUser_whenLastNameIsNull_shouldReturnError() {
        validUser.setLastName(null);

        StepVerifier.create(userUseCase.saveUser(validUser))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El campo 'lastName' es obligatorio"))
                .verify();

        verify(userRepository, never()).saveTransactional(any());
    }

    @Test
    void saveUser_whenEmailIsNull_shouldReturnError() {
        validUser.setEmail(null);

        StepVerifier.create(userUseCase.saveUser(validUser))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El campo 'email' es obligatorio"))
                .verify();

        verify(userRepository, never()).saveTransactional(any());
    }

    @Test
    void saveUser_whenBaseSalaryIsNull_shouldReturnError() {
        validUser.setBaseSalary(null);

        StepVerifier.create(userUseCase.saveUser(validUser))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El campo 'baseSalary' es obligatorio"))
                .verify();

        verify(userRepository, never()).saveTransactional(any());
    }

    @Test
    void saveUser_whenBaseSalaryNegative_shouldReturnError() {
        validUser.setBaseSalary(-1000.0);

        StepVerifier.create(userUseCase.saveUser(validUser))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El salario base debe estar entre 0 y 15.000.000"))
                .verify();

        verify(userRepository, never()).saveTransactional(any());
    }

    @Test
    void saveUser_whenBaseSalaryTooHigh_shouldReturnError() {
        validUser.setBaseSalary(20_000_000.0);

        StepVerifier.create(userUseCase.saveUser(validUser))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El salario base debe estar entre 0 y 15.000.000"))
                .verify();

        verify(userRepository, never()).saveTransactional(any());
    }

    @Test
    void saveUser_whenEmailInvalid_shouldReturnError() {
        validUser.setEmail("correo-invalido");

        StepVerifier.create(userUseCase.saveUser(validUser))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El email tiene un formato inválido"))
                .verify();

        verify(userRepository, never()).saveTransactional(any());
    }

    @Test
    void findAll_shouldReturnUsers() {
        when(userRepository.findAll()).thenReturn(Flux.just(validUser));

        StepVerifier.create(userUseCase.findAll())
                .expectNext(validUser)
                .verifyComplete();

        verify(userRepository, times(1)).findAll();
    }
}
