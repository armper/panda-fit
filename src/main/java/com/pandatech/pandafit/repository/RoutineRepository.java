package com.pandatech.pandafit.repository;

import com.pandatech.pandafit.domain.Routine;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Routine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoutineRepository extends MongoRepository<Routine, String> {}
