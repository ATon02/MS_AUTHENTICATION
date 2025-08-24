package co.com.powerup.r2dbc.adapter;

import co.com.powerup.model.role.Role;
import co.com.powerup.model.role.gateways.RoleRepository;
import co.com.powerup.r2dbc.entity.RoleEntity;
import co.com.powerup.r2dbc.helper.ReactiveAdapterOperations;
import co.com.powerup.r2dbc.repository.RoleReactiveRepository;
import reactor.core.publisher.Mono;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;

@Repository
public class RoleRepositoryAdapter extends ReactiveAdapterOperations<
        Role,
        RoleEntity,
        Long,
        RoleReactiveRepository
> implements RoleRepository {

    private final TransactionalOperator transactionalOperator;

    public RoleRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper,
                                 TransactionalOperator transactionalOperator) {
        super(repository, mapper, d -> mapper.map(d, Role.class));
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<Role> saveTransactional(Role role) {
        RoleEntity entity = mapper.map(role, RoleEntity.class);
        return transactionalOperator.execute(tx -> 
            repository.save(entity)
                      .map(savedEntity -> mapper.map(savedEntity, Role.class))
        ).single();
    }
}

