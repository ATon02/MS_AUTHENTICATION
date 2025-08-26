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
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandler {
    
    private  final IUserUseCase userUseCase;
    private  final UserDTOMapper userDTOMapper;
//private  final UseCase2 useCase2;

    public Mono<ServerResponse> find(ServerRequest serverRequest) {
        log.info("➡️ Ejecutando find() de UserHandler");
        return userUseCase.findAll()
                .map(userDTOMapper::toResponse)
                .collectList()
                .flatMap(users -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(users));
    }

    public Mono<ServerResponse> saveUser(ServerRequest serverRequest) {
        log.info("➡️ Ejecutando saveUser() de UserHandler");
        return serverRequest.bodyToMono(UserCreateDTO.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El body no puede ser null")))
                .map(userDTOMapper::toModel)
                .flatMap(userUseCase::saveUser)
                .map(userDTOMapper::toResponse) 
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUser));
    }

}
