package co.com.powerup.api.role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.powerup.api.dtos.request.RoleCreateDTO;
import co.com.powerup.api.mapper.RoleDTOMapper;
import co.com.powerup.usecase.role.IRoleUseCase;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleHandler {

    private  final IRoleUseCase roleUseCase;
    private  final RoleDTOMapper roleDTOMapper;


    public Mono<ServerResponse> find(ServerRequest serverRequest) {
        log.info("➡️ Entró al handler find() de RoleHandler");
        return roleUseCase.findAll()
                .map(roleDTOMapper::toResponse)
                .collectList()
                .flatMap(roles -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(roles));
    }

    public Mono<ServerResponse> saveRole(ServerRequest serverRequest) {
        log.info("➡️ Entró al handler saveRole() de RoleHandler");
        return serverRequest.bodyToMono(RoleCreateDTO.class)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("El body no puede ser null")))
            .map(roleDTOMapper::toModel)              
            .flatMap(roleUseCase::saveRole)  
            .map(roleDTOMapper::toResponse)         
            .flatMap(role -> ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(role));    
    }
}
