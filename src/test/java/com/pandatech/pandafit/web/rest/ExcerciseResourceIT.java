package com.pandatech.pandafit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pandatech.pandafit.IntegrationTest;
import com.pandatech.pandafit.domain.Excercise;
import com.pandatech.pandafit.domain.enumeration.ExcerciseType;
import com.pandatech.pandafit.repository.ExcerciseRepository;
import com.pandatech.pandafit.repository.search.ExcerciseSearchRepository;
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
 * Integration tests for the {@link ExcerciseResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ExcerciseResourceIT {

    private static final ExcerciseType DEFAULT_TYPE = ExcerciseType.BARBELL;
    private static final ExcerciseType UPDATED_TYPE = ExcerciseType.BAR;

    private static final Integer DEFAULT_CURRENT_VOLUME = 1;
    private static final Integer UPDATED_CURRENT_VOLUME = 2;

    private static final Integer DEFAULT_STARTING_VOLUME = 1;
    private static final Integer UPDATED_STARTING_VOLUME = 2;

    private static final Integer DEFAULT_GOAL_VOLUME = 1;
    private static final Integer UPDATED_GOAL_VOLUME = 2;

    private static final String ENTITY_API_URL = "/api/excercises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/excercises";

    @Autowired
    private ExcerciseRepository excerciseRepository;

    /**
     * This repository is mocked in the com.pandatech.pandafit.repository.search test package.
     *
     * @see com.pandatech.pandafit.repository.search.ExcerciseSearchRepositoryMockConfiguration
     */
    @Autowired
    private ExcerciseSearchRepository mockExcerciseSearchRepository;

    @Autowired
    private MockMvc restExcerciseMockMvc;

    private Excercise excercise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Excercise createEntity() {
        Excercise excercise = new Excercise()
            .type(DEFAULT_TYPE)
            .currentVolume(DEFAULT_CURRENT_VOLUME)
            .startingVolume(DEFAULT_STARTING_VOLUME)
            .goalVolume(DEFAULT_GOAL_VOLUME);
        return excercise;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Excercise createUpdatedEntity() {
        Excercise excercise = new Excercise()
            .type(UPDATED_TYPE)
            .currentVolume(UPDATED_CURRENT_VOLUME)
            .startingVolume(UPDATED_STARTING_VOLUME)
            .goalVolume(UPDATED_GOAL_VOLUME);
        return excercise;
    }

    @BeforeEach
    public void initTest() {
        excerciseRepository.deleteAll();
        excercise = createEntity();
    }

    @Test
    void createExcercise() throws Exception {
        int databaseSizeBeforeCreate = excerciseRepository.findAll().size();
        // Create the Excercise
        restExcerciseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(excercise)))
            .andExpect(status().isCreated());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeCreate + 1);
        Excercise testExcercise = excerciseList.get(excerciseList.size() - 1);
        assertThat(testExcercise.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testExcercise.getCurrentVolume()).isEqualTo(DEFAULT_CURRENT_VOLUME);
        assertThat(testExcercise.getStartingVolume()).isEqualTo(DEFAULT_STARTING_VOLUME);
        assertThat(testExcercise.getGoalVolume()).isEqualTo(DEFAULT_GOAL_VOLUME);

        // Validate the Excercise in Elasticsearch
        verify(mockExcerciseSearchRepository, times(1)).save(testExcercise);
    }

    @Test
    void createExcerciseWithExistingId() throws Exception {
        // Create the Excercise with an existing ID
        excercise.setId("existing_id");

        int databaseSizeBeforeCreate = excerciseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExcerciseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(excercise)))
            .andExpect(status().isBadRequest());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeCreate);

        // Validate the Excercise in Elasticsearch
        verify(mockExcerciseSearchRepository, times(0)).save(excercise);
    }

    @Test
    void getAllExcercises() throws Exception {
        // Initialize the database
        excerciseRepository.save(excercise);

        // Get all the excerciseList
        restExcerciseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(excercise.getId())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].currentVolume").value(hasItem(DEFAULT_CURRENT_VOLUME)))
            .andExpect(jsonPath("$.[*].startingVolume").value(hasItem(DEFAULT_STARTING_VOLUME)))
            .andExpect(jsonPath("$.[*].goalVolume").value(hasItem(DEFAULT_GOAL_VOLUME)));
    }

    @Test
    void getExcercise() throws Exception {
        // Initialize the database
        excerciseRepository.save(excercise);

        // Get the excercise
        restExcerciseMockMvc
            .perform(get(ENTITY_API_URL_ID, excercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(excercise.getId()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.currentVolume").value(DEFAULT_CURRENT_VOLUME))
            .andExpect(jsonPath("$.startingVolume").value(DEFAULT_STARTING_VOLUME))
            .andExpect(jsonPath("$.goalVolume").value(DEFAULT_GOAL_VOLUME));
    }

    @Test
    void getNonExistingExcercise() throws Exception {
        // Get the excercise
        restExcerciseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewExcercise() throws Exception {
        // Initialize the database
        excerciseRepository.save(excercise);

        int databaseSizeBeforeUpdate = excerciseRepository.findAll().size();

        // Update the excercise
        Excercise updatedExcercise = excerciseRepository.findById(excercise.getId()).get();
        updatedExcercise
            .type(UPDATED_TYPE)
            .currentVolume(UPDATED_CURRENT_VOLUME)
            .startingVolume(UPDATED_STARTING_VOLUME)
            .goalVolume(UPDATED_GOAL_VOLUME);

        restExcerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExcercise.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExcercise))
            )
            .andExpect(status().isOk());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeUpdate);
        Excercise testExcercise = excerciseList.get(excerciseList.size() - 1);
        assertThat(testExcercise.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testExcercise.getCurrentVolume()).isEqualTo(UPDATED_CURRENT_VOLUME);
        assertThat(testExcercise.getStartingVolume()).isEqualTo(UPDATED_STARTING_VOLUME);
        assertThat(testExcercise.getGoalVolume()).isEqualTo(UPDATED_GOAL_VOLUME);

        // Validate the Excercise in Elasticsearch
        verify(mockExcerciseSearchRepository).save(testExcercise);
    }

    @Test
    void putNonExistingExcercise() throws Exception {
        int databaseSizeBeforeUpdate = excerciseRepository.findAll().size();
        excercise.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExcerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, excercise.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(excercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Excercise in Elasticsearch
        verify(mockExcerciseSearchRepository, times(0)).save(excercise);
    }

    @Test
    void putWithIdMismatchExcercise() throws Exception {
        int databaseSizeBeforeUpdate = excerciseRepository.findAll().size();
        excercise.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(excercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Excercise in Elasticsearch
        verify(mockExcerciseSearchRepository, times(0)).save(excercise);
    }

    @Test
    void putWithMissingIdPathParamExcercise() throws Exception {
        int databaseSizeBeforeUpdate = excerciseRepository.findAll().size();
        excercise.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcerciseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(excercise)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Excercise in Elasticsearch
        verify(mockExcerciseSearchRepository, times(0)).save(excercise);
    }

    @Test
    void partialUpdateExcerciseWithPatch() throws Exception {
        // Initialize the database
        excerciseRepository.save(excercise);

        int databaseSizeBeforeUpdate = excerciseRepository.findAll().size();

        // Update the excercise using partial update
        Excercise partialUpdatedExcercise = new Excercise();
        partialUpdatedExcercise.setId(excercise.getId());

        restExcerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExcercise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExcercise))
            )
            .andExpect(status().isOk());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeUpdate);
        Excercise testExcercise = excerciseList.get(excerciseList.size() - 1);
        assertThat(testExcercise.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testExcercise.getCurrentVolume()).isEqualTo(DEFAULT_CURRENT_VOLUME);
        assertThat(testExcercise.getStartingVolume()).isEqualTo(DEFAULT_STARTING_VOLUME);
        assertThat(testExcercise.getGoalVolume()).isEqualTo(DEFAULT_GOAL_VOLUME);
    }

    @Test
    void fullUpdateExcerciseWithPatch() throws Exception {
        // Initialize the database
        excerciseRepository.save(excercise);

        int databaseSizeBeforeUpdate = excerciseRepository.findAll().size();

        // Update the excercise using partial update
        Excercise partialUpdatedExcercise = new Excercise();
        partialUpdatedExcercise.setId(excercise.getId());

        partialUpdatedExcercise
            .type(UPDATED_TYPE)
            .currentVolume(UPDATED_CURRENT_VOLUME)
            .startingVolume(UPDATED_STARTING_VOLUME)
            .goalVolume(UPDATED_GOAL_VOLUME);

        restExcerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExcercise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExcercise))
            )
            .andExpect(status().isOk());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeUpdate);
        Excercise testExcercise = excerciseList.get(excerciseList.size() - 1);
        assertThat(testExcercise.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testExcercise.getCurrentVolume()).isEqualTo(UPDATED_CURRENT_VOLUME);
        assertThat(testExcercise.getStartingVolume()).isEqualTo(UPDATED_STARTING_VOLUME);
        assertThat(testExcercise.getGoalVolume()).isEqualTo(UPDATED_GOAL_VOLUME);
    }

    @Test
    void patchNonExistingExcercise() throws Exception {
        int databaseSizeBeforeUpdate = excerciseRepository.findAll().size();
        excercise.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExcerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, excercise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(excercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Excercise in Elasticsearch
        verify(mockExcerciseSearchRepository, times(0)).save(excercise);
    }

    @Test
    void patchWithIdMismatchExcercise() throws Exception {
        int databaseSizeBeforeUpdate = excerciseRepository.findAll().size();
        excercise.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(excercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Excercise in Elasticsearch
        verify(mockExcerciseSearchRepository, times(0)).save(excercise);
    }

    @Test
    void patchWithMissingIdPathParamExcercise() throws Exception {
        int databaseSizeBeforeUpdate = excerciseRepository.findAll().size();
        excercise.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExcerciseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(excercise))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Excercise in Elasticsearch
        verify(mockExcerciseSearchRepository, times(0)).save(excercise);
    }

    @Test
    void deleteExcercise() throws Exception {
        // Initialize the database
        excerciseRepository.save(excercise);

        int databaseSizeBeforeDelete = excerciseRepository.findAll().size();

        // Delete the excercise
        restExcerciseMockMvc
            .perform(delete(ENTITY_API_URL_ID, excercise.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Excercise in Elasticsearch
        verify(mockExcerciseSearchRepository, times(1)).deleteById(excercise.getId());
    }

    @Test
    void searchExcercise() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        excerciseRepository.save(excercise);
        when(mockExcerciseSearchRepository.search(queryStringQuery("id:" + excercise.getId())))
            .thenReturn(Collections.singletonList(excercise));

        // Search the excercise
        restExcerciseMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + excercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(excercise.getId())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].currentVolume").value(hasItem(DEFAULT_CURRENT_VOLUME)))
            .andExpect(jsonPath("$.[*].startingVolume").value(hasItem(DEFAULT_STARTING_VOLUME)))
            .andExpect(jsonPath("$.[*].goalVolume").value(hasItem(DEFAULT_GOAL_VOLUME)));
    }
}
