package co.com.powerup.api.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.powerup.api.dtos.request.LoginRequest;
import co.com.powerup.usecase.auth.IAuthUseCase;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHandler {
    
    private  final IAuthUseCase authUseCase; ;

    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        log.info("➡️ Ejecutando login() de AuthHandler");
        return serverRequest.bodyToMono(LoginRequest.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El body no puede ser null")))
                .flatMap(loginRequest -> 
                    authUseCase.login(loginRequest.email(), loginRequest.password())
                )
                .flatMap(token -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("token", token))
                );
    }


}
