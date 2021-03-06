package com.pandatech.pandafit.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ApplicationUserSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ApplicationUserSearchRepositoryMockConfiguration {

    @MockBean
    private ApplicationUserSearchRepository mockApplicationUserSearchRepository;
}
