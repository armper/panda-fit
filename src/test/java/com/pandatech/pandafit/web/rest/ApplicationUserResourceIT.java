package com.pandatech.pandafit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pandatech.pandafit.IntegrationTest;
import com.pandatech.pandafit.domain.ApplicationUser;
import com.pandatech.pandafit.repository.ApplicationUserRepository;
import com.pandatech.pandafit.repository.search.ApplicationUserSearchRepository;
import java.util.ArrayList;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link ApplicationUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ApplicationUserResourceIT {

    private static final Integer DEFAULT_RANKING = 1;
    private static final Integer UPDATED_RANKING = 2;

    private static final String ENTITY_API_URL = "/api/application-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/application-users";

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Mock
    private ApplicationUserRepository applicationUserRepositoryMock;

    /**
     * This repository is mocked in the com.pandatech.pandafit.repository.search test package.
     *
     * @see com.pandatech.pandafit.repository.search.ApplicationUserSearchRepositoryMockConfiguration
     */
    @Autowired
    private ApplicationUserSearchRepository mockApplicationUserSearchRepository;

    @Autowired
    private MockMvc restApplicationUserMockMvc;

    private ApplicationUser applicationUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationUser createEntity() {
        ApplicationUser applicationUser = new ApplicationUser().ranking(DEFAULT_RANKING);
        return applicationUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationUser createUpdatedEntity() {
        ApplicationUser applicationUser = new ApplicationUser().ranking(UPDATED_RANKING);
        return applicationUser;
    }

    @BeforeEach
    public void initTest() {
        applicationUserRepository.deleteAll();
        applicationUser = createEntity();
    }

    @Test
    void createApplicationUser() throws Exception {
        int databaseSizeBeforeCreate = applicationUserRepository.findAll().size();
        // Create the ApplicationUser
        restApplicationUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isCreated());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getRanking()).isEqualTo(DEFAULT_RANKING);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(1)).save(testApplicationUser);
    }

    @Test
    void createApplicationUserWithExistingId() throws Exception {
        // Create the ApplicationUser with an existing ID
        applicationUser.setId("existing_id");

        int databaseSizeBeforeCreate = applicationUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeCreate);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(0)).save(applicationUser);
    }

    @Test
    void getAllApplicationUsers() throws Exception {
        // Initialize the database
        applicationUserRepository.save(applicationUser);

        // Get all the applicationUserList
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationUser.getId())))
            .andExpect(jsonPath("$.[*].ranking").value(hasItem(DEFAULT_RANKING)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApplicationUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(applicationUserRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restApplicationUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(applicationUserRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApplicationUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(applicationUserRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restApplicationUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(applicationUserRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.save(applicationUser);

        // Get the applicationUser
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL_ID, applicationUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicationUser.getId()))
            .andExpect(jsonPath("$.ranking").value(DEFAULT_RANKING));
    }

    @Test
    void getNonExistingApplicationUser() throws Exception {
        // Get the applicationUser
        restApplicationUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.save(applicationUser);

        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();

        // Update the applicationUser
        ApplicationUser updatedApplicationUser = applicationUserRepository.findById(applicationUser.getId()).get();
        updatedApplicationUser.ranking(UPDATED_RANKING);

        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApplicationUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApplicationUser))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getRanking()).isEqualTo(UPDATED_RANKING);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository).save(testApplicationUser);
    }

    @Test
    void putNonExistingApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(0)).save(applicationUser);
    }

    @Test
    void putWithIdMismatchApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(0)).save(applicationUser);
    }

    @Test
    void putWithMissingIdPathParamApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(0)).save(applicationUser);
    }

    @Test
    void partialUpdateApplicationUserWithPatch() throws Exception {
        // Initialize the database
        applicationUserRepository.save(applicationUser);

        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();

        // Update the applicationUser using partial update
        ApplicationUser partialUpdatedApplicationUser = new ApplicationUser();
        partialUpdatedApplicationUser.setId(applicationUser.getId());

        partialUpdatedApplicationUser.ranking(UPDATED_RANKING);

        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicationUser))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getRanking()).isEqualTo(UPDATED_RANKING);
    }

    @Test
    void fullUpdateApplicationUserWithPatch() throws Exception {
        // Initialize the database
        applicationUserRepository.save(applicationUser);

        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();

        // Update the applicationUser using partial update
        ApplicationUser partialUpdatedApplicationUser = new ApplicationUser();
        partialUpdatedApplicationUser.setId(applicationUser.getId());

        partialUpdatedApplicationUser.ranking(UPDATED_RANKING);

        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicationUser))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getRanking()).isEqualTo(UPDATED_RANKING);
    }

    @Test
    void patchNonExistingApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, applicationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(0)).save(applicationUser);
    }

    @Test
    void patchWithIdMismatchApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(0)).save(applicationUser);
    }

    @Test
    void patchWithMissingIdPathParamApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(0)).save(applicationUser);
    }

    @Test
    void deleteApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.save(applicationUser);

        int databaseSizeBeforeDelete = applicationUserRepository.findAll().size();

        // Delete the applicationUser
        restApplicationUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, applicationUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(1)).deleteById(applicationUser.getId());
    }

    @Test
    void searchApplicationUser() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        applicationUserRepository.save(applicationUser);
        when(mockApplicationUserSearchRepository.search(queryStringQuery("id:" + applicationUser.getId())))
            .thenReturn(Collections.singletonList(applicationUser));

        // Search the applicationUser
        restApplicationUserMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + applicationUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationUser.getId())))
            .andExpect(jsonPath("$.[*].ranking").value(hasItem(DEFAULT_RANKING)));
    }
}
