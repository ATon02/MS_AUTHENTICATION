package co.com.powerup.api.role;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.powerup.api.config.JwtAuthenticationFilter;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.util.List;

@Configuration
public class RoleRouter {
    @Bean
    public RouterFunction<ServerResponse> roleRouterFunction(RoleHandler roleHandler, JwtAuthenticationFilter filter) {
        RouterFunction<ServerResponse> find = route(GET("/api/v1/roles"), roleHandler::find)
                .filter(filter.requireRole(List.of("admin")));
        RouterFunction<ServerResponse> save = route(POST("/api/v1/roles"), roleHandler::saveRole)
                .filter(filter.requireRole(List.of("admin")));
        return find.and(save);
    }
}
