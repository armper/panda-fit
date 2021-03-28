package com.pandatech.pandafit.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ExcerciseSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ExcerciseSearchRepositoryMockConfiguration {

    @MockBean
    private ExcerciseSearchRepository mockExcerciseSearchRepository;
}
