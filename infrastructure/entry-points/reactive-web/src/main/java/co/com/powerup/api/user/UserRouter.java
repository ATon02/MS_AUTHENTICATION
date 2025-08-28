package co.com.powerup.api.user;

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
public class UserRouter {
    @Bean
    public RouterFunction<ServerResponse> userRouterFunction(UserHandler userhandler, JwtAuthenticationFilter filter) {
        return route(GET("/api/v1/users"), userhandler::find).filter(filter.requireRole(List.of("admin","asesor"))) 
                .andRoute(POST("/api/v1/users"), userhandler::saveUser).filter(filter.requireRole(List.of("admin","asesor")))
                .andRoute(POST("/api/v1/users/admin"), userhandler::saveUserAdmin).filter(filter.requireRole(List.of("admin")));
    }
}
