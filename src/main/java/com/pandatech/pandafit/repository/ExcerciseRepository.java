package com.pandatech.pandafit.repository;

import com.pandatech.pandafit.domain.Excercise;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Excercise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExcerciseRepository extends MongoRepository<Excercise, String> {}
