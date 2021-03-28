package com.pandatech.pandafit.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link RoutineSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class RoutineSearchRepositoryMockConfiguration {

    @MockBean
    private RoutineSearchRepository mockRoutineSearchRepository;
}
