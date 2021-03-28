package com.pandatech.pandafit.repository.search;

import com.pandatech.pandafit.domain.Excercise;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Excercise} entity.
 */
public interface ExcerciseSearchRepository extends ElasticsearchRepository<Excercise, String> {}
