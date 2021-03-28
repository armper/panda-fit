package com.pandatech.pandafit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pandatech.pandafit.IntegrationTest;
import com.pandatech.pandafit.domain.Cycle;
import com.pandatech.pandafit.repository.CycleRepository;
import com.pandatech.pandafit.repository.search.CycleSearchRepository;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link CycleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CycleResourceIT {

    private static final Integer DEFAULT_REPS = 1;
    private static final Integer UPDATED_REPS = 2;

    private static final Integer DEFAULT_VOLUME = 1;
    private static final Integer UPDATED_VOLUME = 2;

    private static final String ENTITY_API_URL = "/api/cycles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/cycles";

    @Autowired
    private CycleRepository cycleRepository;

    /**
     * This repository is mocked in the com.pandatech.pandafit.repository.search test package.
     *
     * @see com.pandatech.pandafit.repository.search.CycleSearchRepositoryMockConfiguration
     */
    @Autowired
    private CycleSearchRepository mockCycleSearchRepository;

    @Autowired
    private MockMvc restCycleMockMvc;

    private Cycle cycle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cycle createEntity() {
        Cycle cycle = new Cycle().reps(DEFAULT_REPS).volume(DEFAULT_VOLUME);
        return cycle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cycle createUpdatedEntity() {
        Cycle cycle = new Cycle().reps(UPDATED_REPS).volume(UPDATED_VOLUME);
        return cycle;
    }

    @BeforeEach
    public void initTest() {
        cycleRepository.deleteAll();
        cycle = createEntity();
    }

    @Test
    void createCycle() throws Exception {
        int databaseSizeBeforeCreate = cycleRepository.findAll().size();
        // Create the Cycle
        restCycleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cycle)))
            .andExpect(status().isCreated());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeCreate + 1);
        Cycle testCycle = cycleList.get(cycleList.size() - 1);
        assertThat(testCycle.getReps()).isEqualTo(DEFAULT_REPS);
        assertThat(testCycle.getVolume()).isEqualTo(DEFAULT_VOLUME);

        // Validate the Cycle in Elasticsearch
        verify(mockCycleSearchRepository, times(1)).save(testCycle);
    }

    @Test
    void createCycleWithExistingId() throws Exception {
        // Create the Cycle with an existing ID
        cycle.setId("existing_id");

        int databaseSizeBeforeCreate = cycleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCycleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cycle)))
            .andExpect(status().isBadRequest());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeCreate);

        // Validate the Cycle in Elasticsearch
        verify(mockCycleSearchRepository, times(0)).save(cycle);
    }

    @Test
    void getAllCycles() throws Exception {
        // Initialize the database
        cycleRepository.save(cycle);

        // Get all the cycleList
        restCycleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cycle.getId())))
            .andExpect(jsonPath("$.[*].reps").value(hasItem(DEFAULT_REPS)))
            .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME)));
    }

    @Test
    void getCycle() throws Exception {
        // Initialize the database
        cycleRepository.save(cycle);

        // Get the cycle
        restCycleMockMvc
            .perform(get(ENTITY_API_URL_ID, cycle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cycle.getId()))
            .andExpect(jsonPath("$.reps").value(DEFAULT_REPS))
            .andExpect(jsonPath("$.volume").value(DEFAULT_VOLUME));
    }

    @Test
    void getNonExistingCycle() throws Exception {
        // Get the cycle
        restCycleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewCycle() throws Exception {
        // Initialize the database
        cycleRepository.save(cycle);

        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();

        // Update the cycle
        Cycle updatedCycle = cycleRepository.findById(cycle.getId()).get();
        updatedCycle.reps(UPDATED_REPS).volume(UPDATED_VOLUME);

        restCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCycle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCycle))
            )
            .andExpect(status().isOk());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);
        Cycle testCycle = cycleList.get(cycleList.size() - 1);
        assertThat(testCycle.getReps()).isEqualTo(UPDATED_REPS);
        assertThat(testCycle.getVolume()).isEqualTo(UPDATED_VOLUME);

        // Validate the Cycle in Elasticsearch
        verify(mockCycleSearchRepository).save(testCycle);
    }

    @Test
    void putNonExistingCycle() throws Exception {
        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();
        cycle.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cycle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cycle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Cycle in Elasticsearch
        verify(mockCycleSearchRepository, times(0)).save(cycle);
    }

    @Test
    void putWithIdMismatchCycle() throws Exception {
        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();
        cycle.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cycle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Cycle in Elasticsearch
        verify(mockCycleSearchRepository, times(0)).save(cycle);
    }

    @Test
    void putWithMissingIdPathParamCycle() throws Exception {
        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();
        cycle.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cycle)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Cycle in Elasticsearch
        verify(mockCycleSearchRepository, times(0)).save(cycle);
    }

    @Test
    void partialUpdateCycleWithPatch() throws Exception {
        // Initialize the database
        cycleRepository.save(cycle);

        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();

        // Update the cycle using partial update
        Cycle partialUpdatedCycle = new Cycle();
        partialUpdatedCycle.setId(cycle.getId());

        restCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCycle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCycle))
            )
            .andExpect(status().isOk());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);
        Cycle testCycle = cycleList.get(cycleList.size() - 1);
        assertThat(testCycle.getReps()).isEqualTo(DEFAULT_REPS);
        assertThat(testCycle.getVolume()).isEqualTo(DEFAULT_VOLUME);
    }

    @Test
    void fullUpdateCycleWithPatch() throws Exception {
        // Initialize the database
        cycleRepository.save(cycle);

        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();

        // Update the cycle using partial update
        Cycle partialUpdatedCycle = new Cycle();
        partialUpdatedCycle.setId(cycle.getId());

        partialUpdatedCycle.reps(UPDATED_REPS).volume(UPDATED_VOLUME);

        restCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCycle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCycle))
            )
            .andExpect(status().isOk());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);
        Cycle testCycle = cycleList.get(cycleList.size() - 1);
        assertThat(testCycle.getReps()).isEqualTo(UPDATED_REPS);
        assertThat(testCycle.getVolume()).isEqualTo(UPDATED_VOLUME);
    }

    @Test
    void patchNonExistingCycle() throws Exception {
        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();
        cycle.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cycle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cycle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Cycle in Elasticsearch
        verify(mockCycleSearchRepository, times(0)).save(cycle);
    }

    @Test
    void patchWithIdMismatchCycle() throws Exception {
        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();
        cycle.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cycle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Cycle in Elasticsearch
        verify(mockCycleSearchRepository, times(0)).save(cycle);
    }

    @Test
    void patchWithMissingIdPathParamCycle() throws Exception {
        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();
        cycle.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cycle)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Cycle in Elasticsearch
        verify(mockCycleSearchRepository, times(0)).save(cycle);
    }

    @Test
    void deleteCycle() throws Exception {
        // Initialize the database
        cycleRepository.save(cycle);

        int databaseSizeBeforeDelete = cycleRepository.findAll().size();

        // Delete the cycle
        restCycleMockMvc
            .perform(delete(ENTITY_API_URL_ID, cycle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Cycle in Elasticsearch
        verify(mockCycleSearchRepository, times(1)).deleteById(cycle.getId());
    }

    @Test
    void searchCycle() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        cycleRepository.save(cycle);
        when(mockCycleSearchRepository.search(queryStringQuery("id:" + cycle.getId()))).thenReturn(Collections.singletonList(cycle));

        // Search the cycle
        restCycleMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + cycle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cycle.getId())))
            .andExpect(jsonPath("$.[*].reps").value(hasItem(DEFAULT_REPS)))
            .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME)));
    }
}
