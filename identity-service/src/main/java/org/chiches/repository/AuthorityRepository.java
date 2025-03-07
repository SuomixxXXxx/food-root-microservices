package org.chiches.repository;

import org.chiches.entity.AuthorityEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AuthorityRepository extends GeneralRepository<AuthorityEntity, Long>{
    boolean existsByAuthority(String authority);

    List<AuthorityEntity> findByAuthorityContainingIgnoreCase(String authority);
}
