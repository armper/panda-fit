package com.pandatech.pandafit.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.pandatech.pandafit.domain.Excercise;
import com.pandatech.pandafit.repository.ExcerciseRepository;
import com.pandatech.pandafit.repository.search.ExcerciseSearchRepository;
import com.pandatech.pandafit.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.pandatech.pandafit.domain.Excercise}.
 */
@RestController
@RequestMapping("/api")
public class ExcerciseResource {

    private final Logger log = LoggerFactory.getLogger(ExcerciseResource.class);

    private static final String ENTITY_NAME = "excercise";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExcerciseRepository excerciseRepository;

    private final ExcerciseSearchRepository excerciseSearchRepository;

    public ExcerciseResource(ExcerciseRepository excerciseRepository, ExcerciseSearchRepository excerciseSearchRepository) {
        this.excerciseRepository = excerciseRepository;
        this.excerciseSearchRepository = excerciseSearchRepository;
    }

    /**
     * {@code POST  /excercises} : Create a new excercise.
     *
     * @param excercise the excercise to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new excercise, or with status {@code 400 (Bad Request)} if the excercise has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/excercises")
    public ResponseEntity<Excercise> createExcercise(@RequestBody Excercise excercise) throws URISyntaxException {
        log.debug("REST request to save Excercise : {}", excercise);
        if (excercise.getId() != null) {
            throw new BadRequestAlertException("A new excercise cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Excercise result = excerciseRepository.save(excercise);
        excerciseSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/excercises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /excercises/:id} : Updates an existing excercise.
     *
     * @param id the id of the excercise to save.
     * @param excercise the excercise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated excercise,
     * or with status {@code 400 (Bad Request)} if the excercise is not valid,
     * or with status {@code 500 (Internal Server Error)} if the excercise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/excercises/{id}")
    public ResponseEntity<Excercise> updateExcercise(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Excercise excercise
    ) throws URISyntaxException {
        log.debug("REST request to update Excercise : {}, {}", id, excercise);
        if (excercise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, excercise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!excerciseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Excercise result = excerciseRepository.save(excercise);
        excerciseSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, excercise.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /excercises/:id} : Partial updates given fields of an existing excercise, field will ignore if it is null
     *
     * @param id the id of the excercise to save.
     * @param excercise the excercise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated excercise,
     * or with status {@code 400 (Bad Request)} if the excercise is not valid,
     * or with status {@code 404 (Not Found)} if the excercise is not found,
     * or with status {@code 500 (Internal Server Error)} if the excercise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/excercises/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Excercise> partialUpdateExcercise(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Excercise excercise
    ) throws URISyntaxException {
        log.debug("REST request to partial update Excercise partially : {}, {}", id, excercise);
        if (excercise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, excercise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!excerciseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Excercise> result = excerciseRepository
            .findById(excercise.getId())
            .map(
                existingExcercise -> {
                    if (excercise.getType() != null) {
                        existingExcercise.setType(excercise.getType());
                    }
                    if (excercise.getCurrentVolume() != null) {
                        existingExcercise.setCurrentVolume(excercise.getCurrentVolume());
                    }
                    if (excercise.getStartingVolume() != null) {
                        existingExcercise.setStartingVolume(excercise.getStartingVolume());
                    }
                    if (excercise.getGoalVolume() != null) {
                        existingExcercise.setGoalVolume(excercise.getGoalVolume());
                    }

                    return existingExcercise;
                }
            )
            .map(excerciseRepository::save)
            .map(
                savedExcercise -> {
                    excerciseSearchRepository.save(savedExcercise);

                    return savedExcercise;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, excercise.getId())
        );
    }

    /**
     * {@code GET  /excercises} : get all the excercises.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of excercises in body.
     */
    @GetMapping("/excercises")
    public List<Excercise> getAllExcercises() {
        log.debug("REST request to get all Excercises");
        return excerciseRepository.findAll();
    }

    /**
     * {@code GET  /excercises/:id} : get the "id" excercise.
     *
     * @param id the id of the excercise to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the excercise, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/excercises/{id}")
    public ResponseEntity<Excercise> getExcercise(@PathVariable String id) {
        log.debug("REST request to get Excercise : {}", id);
        Optional<Excercise> excercise = excerciseRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(excercise);
    }

    /**
     * {@code DELETE  /excercises/:id} : delete the "id" excercise.
     *
     * @param id the id of the excercise to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/excercises/{id}")
    public ResponseEntity<Void> deleteExcercise(@PathVariable String id) {
        log.debug("REST request to delete Excercise : {}", id);
        excerciseRepository.deleteById(id);
        excerciseSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/excercises?query=:query} : search for the excercise corresponding
     * to the query.
     *
     * @param query the query of the excercise search.
     * @return the result of the search.
     */
    @GetMapping("/_search/excercises")
    public List<Excercise> searchExcercises(@RequestParam String query) {
        log.debug("REST request to search Excercises for query {}", query);
        return StreamSupport
            .stream(excerciseSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
