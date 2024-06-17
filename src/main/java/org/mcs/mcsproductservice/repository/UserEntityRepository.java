package org.mcs.mcsproductservice.repository;

import org.mcs.mcsproductservice.model.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findUserEntityByUsername(String username);
}
