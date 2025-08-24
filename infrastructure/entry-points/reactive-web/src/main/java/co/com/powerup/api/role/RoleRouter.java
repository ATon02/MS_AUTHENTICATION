package co.com.powerup.api.role;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RoleRouter {
    @Bean
    public RouterFunction<ServerResponse> roleRouterFunction(RoleHandler roleHandler) {
        return route(GET("/api/v1/roles"), roleHandler::find)
                .andRoute(POST("/api/v1/roles"), roleHandler::saveRole);
    }
}
