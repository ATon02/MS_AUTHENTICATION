package co.com.powerup.passwordencoder;

import org.springframework.stereotype.Component;
import co.com.powerup.usecase.passwordencoder.PasswordEncoderUseCase;
import reactor.core.publisher.Mono;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Component
public class PasswordEncoderService implements PasswordEncoderUseCase {


    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public Mono<String> encode(String password) {
        return Mono.just(encoder.encode(password));
    }

    @Override
    public Mono<Boolean> matches(String password, String encodedPassword) {
        return Mono.just(encoder.matches(password, encodedPassword));
    }
}