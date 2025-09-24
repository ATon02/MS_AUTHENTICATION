package co.com.powerup.api.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.powerup.api.dtos.request.UserCreateDTO;
import co.com.powerup.api.mapper.UserDTOMapper;
import co.com.powerup.usecase.user.IUserUseCase;
import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandler {
    
    private  final IUserUseCase userUseCase;
    private  final UserDTOMapper userDTOMapper;

    public Mono<ServerResponse> find(ServerRequest serverRequest) {
        log.info(" Ejecutando find() de UserHandler");
        return userUseCase.findAll()
                .map(userDTOMapper::toResponse)
                .collectList()
                .flatMap(users -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(users));
    }

    public Mono<ServerResponse> saveUser(ServerRequest serverRequest) {
        log.info(" Ejecutando saveUser() de UserHandler");
        return serverRequest.bodyToMono(UserCreateDTO.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El body no puede ser null")))
                .map(userDTOMapper::toModel)
                .flatMap(userUseCase::saveUser)
                .map(userDTOMapper::toResponse) 
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUser));
    }

    public Mono<ServerResponse> saveUserAdmin(ServerRequest serverRequest) {
        log.info(" Ejecutando saveUserAdmin() de UserHandler");
        return serverRequest.bodyToMono(UserCreateDTO.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El body no puede ser null")))
                .map(userDTOMapper::toModel)
                .flatMap(userUseCase::saveUserAdmin)
                .map(userDTOMapper::toResponse) 
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUser));
    }

    public Mono<ServerResponse> findByEmail(ServerRequest request) {
        log.info(" Ejecutando findByEmail() de UserHandler");
        return Mono.justOrEmpty(request.queryParam("email"))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El campo 'email' es obligatorio")))
                .flatMap(userUseCase::findByEmail)
                .map(userDTOMapper::toResponse)
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user));
    }

    @SuppressWarnings("null")
    public Mono<ServerResponse> selfSearch(ServerRequest request) {
        log.info(" Ejecutando selfSearch() de UserHandler");
        Claims claims = (Claims) request.exchange().getAttribute("claims");
        String emailSub = claims.getSubject();
        return userUseCase.findByEmail(emailSub)
                .map(userDTOMapper::toResponse)
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user));
    }

    public Mono<ServerResponse> findByRole(ServerRequest serverRequest) {
        log.info(" Ejecutando findByRole() de UserHandler");
        Long roleId = serverRequest.queryParam("roleId")
                .map(Long::parseLong)
                .orElseThrow(() -> new IllegalArgumentException("roleId es requerido"));
        return userUseCase.findByRole(roleId)
                .map(userDTOMapper::toResponse)
                .collectList()
                .flatMap(users -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(users));
    }

}
