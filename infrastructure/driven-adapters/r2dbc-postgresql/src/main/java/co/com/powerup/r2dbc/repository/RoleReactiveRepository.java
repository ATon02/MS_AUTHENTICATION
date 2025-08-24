package co.com.powerup.r2dbc.repository;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import co.com.powerup.r2dbc.entity.RoleEntity;

// TODO: This file is just an example, you should delete or modify it
@Repository
public interface RoleReactiveRepository extends ReactiveCrudRepository<RoleEntity, Long>, ReactiveQueryByExampleExecutor<RoleEntity> {

}
