package com.pandatech.pandafit.repository.search;

import com.pandatech.pandafit.domain.Cycle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Cycle} entity.
 */
public interface CycleSearchRepository extends ElasticsearchRepository<Cycle, String> {}
