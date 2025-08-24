package co.com.powerup.r2dbc.adapter;

import co.com.powerup.model.user.User;
import co.com.powerup.model.user.gateways.UserRepository;
import co.com.powerup.r2dbc.entity.UserEntity;
import co.com.powerup.r2dbc.helper.ReactiveAdapterOperations;
import co.com.powerup.r2dbc.repository.UserReactiveRepository;
import reactor.core.publisher.Mono;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;

@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<
    User/* change for domain model */,
    UserEntity/* change for adapter model */,
    Long,
    UserReactiveRepository
> implements UserRepository  {

    private final TransactionalOperator transactionalOperator;


    public UserRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper,
                                    TransactionalOperator transactionalOperator) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, User.class/* change for domain model */));
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<User> saveTransactional(User user) {
        UserEntity entity = mapper.map(user, UserEntity.class);
        return transactionalOperator.execute(tx -> 
            repository.save(entity)
                      .map(savedEntity -> mapper.map(savedEntity, User.class))
        ).single();
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email)
                         .map(entity -> mapper.map(entity, User.class));
    }

}
