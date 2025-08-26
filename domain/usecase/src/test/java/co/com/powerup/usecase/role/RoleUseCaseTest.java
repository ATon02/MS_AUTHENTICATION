package co.com.powerup.usecase.role;

import co.com.powerup.model.role.Role;
import co.com.powerup.model.role.gateways.RoleRepository;
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
class RoleUseCaseTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleUseCase roleUseCase;

    private Role validRole;

    @BeforeEach
    void setUp() {
        validRole = new Role();
        validRole.setName("ADMIN");
        validRole.setDescription("Administrador del sistema");
    }

    @Test
    void saveRole_whenValidRole_shouldSaveSuccessfully() {
        when(roleRepository.saveTransactional(any(Role.class))).thenReturn(Mono.just(validRole));

        StepVerifier.create(roleUseCase.saveRole(validRole))
                .expectNextMatches(role -> role.getName().equals("ADMIN") &&
                                           role.getDescription().equals("Administrador del sistema"))
                .verifyComplete();

        verify(roleRepository, times(1)).saveTransactional(validRole);
    }

    @Test
    void saveRole_whenNameIsNull_shouldReturnError() {
        validRole.setName(null);

        StepVerifier.create(roleUseCase.saveRole(validRole))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El campo 'name' es obligatorio"))
                .verify();

        verify(roleRepository, never()).saveTransactional(any());
    }

    @Test
    void saveRole_whenNameIsBlank_shouldReturnError() {
        validRole.setName(" ");

        StepVerifier.create(roleUseCase.saveRole(validRole))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El campo 'name' es obligatorio"))
                .verify();

        verify(roleRepository, never()).saveTransactional(any());
    }

    @Test
    void saveRole_whenDescriptionIsNull_shouldReturnError() {
        validRole.setDescription(null);

        StepVerifier.create(roleUseCase.saveRole(validRole))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El campo 'description' es obligatorio"))
                .verify();

        verify(roleRepository, never()).saveTransactional(any());
    }

    @Test
    void saveRole_whenDescriptionIsBlank_shouldReturnError() {
        validRole.setDescription(" ");

        StepVerifier.create(roleUseCase.saveRole(validRole))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El campo 'description' es obligatorio"))
                .verify();

        verify(roleRepository, never()).saveTransactional(any());
    }

    @Test
    void findAll_shouldReturnRoles() {
        when(roleRepository.findAll()).thenReturn(Flux.just(validRole));

        StepVerifier.create(roleUseCase.findAll())
                .expectNext(validRole)
                .verifyComplete();

        verify(roleRepository, times(1)).findAll();
    }
}

