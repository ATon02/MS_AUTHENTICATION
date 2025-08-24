package co.com.powerup.usecase.user;

import co.com.powerup.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserUseCase {

    Mono<User> saveUser(User user);
    Flux<User> findAll();

}
