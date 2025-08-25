package co.com.powerup.api.role;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.powerup.api.dtos.request.RoleCreateDTO;
import co.com.powerup.api.mapper.RoleDTOMapper;
import co.com.powerup.usecase.role.IRoleUseCase;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RoleHandler {

    private  final IRoleUseCase roleUseCase;
    private  final RoleDTOMapper roleDTOMapper;


    public Mono<ServerResponse> find(ServerRequest serverRequest) {
        System.out.println("➡️ Entró al handler find() de RoleHandler");
        return roleUseCase.findAll()
                .map(roleDTOMapper::toResponse)
                .collectList()
                .flatMap(roles -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(roles))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("error", e.getMessage())));
    }

    public Mono<ServerResponse> saveRole(ServerRequest serverRequest) {
        System.out.println("➡️ Entró al handler saveRole() de RoleHandler");
        return serverRequest.bodyToMono(RoleCreateDTO.class)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("El body no puede ser null")))
            .map(roleDTOMapper::toModel)              
            .flatMap(roleUseCase::saveRole)  
            .map(roleDTOMapper::toResponse)         
            .flatMap(role -> ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(role))
            .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("error", e.getMessage())));    
    }
}
