package org.mcs.mcsproductservice.repository;

import org.mcs.mcsproductservice.model.UserAuthority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthorityRepository extends CrudRepository<UserAuthority, Long> {

    UserAuthority getUserAuthoritiesByUserId(Long userId);
}
