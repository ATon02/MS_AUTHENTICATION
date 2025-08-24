package co.com.powerup.r2dbc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;

import io.r2dbc.spi.ConnectionFactory;

@Configuration
public class DatabaseConfig {

    @Bean
    R2dbcTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }
}
