package com.pandatech.pandafit.repository.search;

import com.pandatech.pandafit.domain.Routine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Routine} entity.
 */
public interface RoutineSearchRepository extends ElasticsearchRepository<Routine, String> {}
