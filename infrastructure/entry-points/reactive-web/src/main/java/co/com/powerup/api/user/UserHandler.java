package co.com.powerup.api.user;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.powerup.api.dtos.request.UserCreateDTO;
import co.com.powerup.api.mapper.UserDTOMapper;
import co.com.powerup.usecase.user.IUserUseCase;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {
    
    private  final IUserUseCase userUseCase;
    private  final UserDTOMapper userDTOMapper;
//private  final UseCase2 useCase2;

    public Mono<ServerResponse> find(ServerRequest serverRequest) {
        System.out.println("➡️ Ejecutando find() de UserHandler");
        return userUseCase.findAll()
                .map(userDTOMapper::toResponse)
                .collectList()
                .flatMap(users -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(users))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("error", e.getMessage())));
    }

    public Mono<ServerResponse> saveUser(ServerRequest serverRequest) {
        System.out.println("➡️ Ejecutando saveUser() de UserHandler");
        return serverRequest.bodyToMono(UserCreateDTO.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El body no puede ser null")))
                .map(userDTOMapper::toModel)
                .flatMap(userUseCase::saveUser)
                .map(userDTOMapper::toResponse) 
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUser))
                .onErrorResume(e -> {
                    if (e instanceof IllegalArgumentException) {
                        return ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(Map.of("error", e.getMessage()));
                    }
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(Map.of("error", e.getMessage()));
                });
    }

}
