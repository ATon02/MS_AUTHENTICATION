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
        RouterFunction<ServerResponse> find = route(GET("/api/v1/users"), userhandler::find)
                .filter(filter.requireRole(List.of("admin","asesor")));
        RouterFunction<ServerResponse> save = route(POST("/api/v1/users"), userhandler::saveUser)
                .filter(filter.requireRole(List.of("admin","asesor")));
        RouterFunction<ServerResponse> saveAdmin = route(POST("/api/v1/users/admin"), userhandler::saveUserAdmin)
                .filter(filter.requireRole(List.of("admin")));
        RouterFunction<ServerResponse> findByEmail = route(GET("/api/v1/users/find-by-email"), userhandler::findByEmail)
                .filter(filter.requireRole(List.of("admin","asesor")));
        RouterFunction<ServerResponse> selfSearch = route(GET("/api/v1/users/find-by-email/self"), userhandler::selfSearch)
                .filter(filter.requireRole(List.of("admin","asesor","cliente")));
        RouterFunction<ServerResponse> findByRole = route(GET("/api/v1/users/find-by-role"), userhandler::findByRole);
        return find.and(save).and(saveAdmin).and(findByEmail).and(selfSearch).and(findByRole);
    }
}
