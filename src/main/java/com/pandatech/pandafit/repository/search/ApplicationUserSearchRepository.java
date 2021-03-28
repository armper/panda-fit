package com.pandatech.pandafit.repository.search;

import com.pandatech.pandafit.domain.ApplicationUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ApplicationUser} entity.
 */
public interface ApplicationUserSearchRepository extends ElasticsearchRepository<ApplicationUser, String> {}
