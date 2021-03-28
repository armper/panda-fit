package com.pandatech.pandafit.repository;

import com.pandatech.pandafit.domain.ApplicationUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ApplicationUser entity.
 */
@Repository
public interface ApplicationUserRepository extends MongoRepository<ApplicationUser, String> {
    @Query("{}")
    Page<ApplicationUser> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<ApplicationUser> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<ApplicationUser> findOneWithEagerRelationships(String id);
}
