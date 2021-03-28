package com.pandatech.pandafit.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.pandatech.pandafit.domain.Routine;
import com.pandatech.pandafit.repository.RoutineRepository;
import com.pandatech.pandafit.repository.search.RoutineSearchRepository;
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
 * REST controller for managing {@link com.pandatech.pandafit.domain.Routine}.
 */
@RestController
@RequestMapping("/api")
public class RoutineResource {

    private final Logger log = LoggerFactory.getLogger(RoutineResource.class);

    private static final String ENTITY_NAME = "routine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoutineRepository routineRepository;

    private final RoutineSearchRepository routineSearchRepository;

    public RoutineResource(RoutineRepository routineRepository, RoutineSearchRepository routineSearchRepository) {
        this.routineRepository = routineRepository;
        this.routineSearchRepository = routineSearchRepository;
    }

    /**
     * {@code POST  /routines} : Create a new routine.
     *
     * @param routine the routine to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new routine, or with status {@code 400 (Bad Request)} if the routine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/routines")
    public ResponseEntity<Routine> createRoutine(@RequestBody Routine routine) throws URISyntaxException {
        log.debug("REST request to save Routine : {}", routine);
        if (routine.getId() != null) {
            throw new BadRequestAlertException("A new routine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Routine result = routineRepository.save(routine);
        routineSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/routines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /routines/:id} : Updates an existing routine.
     *
     * @param id the id of the routine to save.
     * @param routine the routine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated routine,
     * or with status {@code 400 (Bad Request)} if the routine is not valid,
     * or with status {@code 500 (Internal Server Error)} if the routine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/routines/{id}")
    public ResponseEntity<Routine> updateRoutine(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Routine routine
    ) throws URISyntaxException {
        log.debug("REST request to update Routine : {}, {}", id, routine);
        if (routine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, routine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!routineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Routine result = routineRepository.save(routine);
        routineSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, routine.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /routines/:id} : Partial updates given fields of an existing routine, field will ignore if it is null
     *
     * @param id the id of the routine to save.
     * @param routine the routine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated routine,
     * or with status {@code 400 (Bad Request)} if the routine is not valid,
     * or with status {@code 404 (Not Found)} if the routine is not found,
     * or with status {@code 500 (Internal Server Error)} if the routine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/routines/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Routine> partialUpdateRoutine(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Routine routine
    ) throws URISyntaxException {
        log.debug("REST request to partial update Routine partially : {}, {}", id, routine);
        if (routine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, routine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!routineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Routine> result = routineRepository
            .findById(routine.getId())
            .map(
                existingRoutine -> {
                    if (routine.getName() != null) {
                        existingRoutine.setName(routine.getName());
                    }
                    if (routine.getDateStarted() != null) {
                        existingRoutine.setDateStarted(routine.getDateStarted());
                    }
                    if (routine.getDateEnded() != null) {
                        existingRoutine.setDateEnded(routine.getDateEnded());
                    }
                    if (routine.getGoalDate() != null) {
                        existingRoutine.setGoalDate(routine.getGoalDate());
                    }
                    if (routine.getStartingBodyWeight() != null) {
                        existingRoutine.setStartingBodyWeight(routine.getStartingBodyWeight());
                    }
                    if (routine.getEndingBodyWeight() != null) {
                        existingRoutine.setEndingBodyWeight(routine.getEndingBodyWeight());
                    }

                    return existingRoutine;
                }
            )
            .map(routineRepository::save)
            .map(
                savedRoutine -> {
                    routineSearchRepository.save(savedRoutine);

                    return savedRoutine;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, routine.getId())
        );
    }

    /**
     * {@code GET  /routines} : get all the routines.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of routines in body.
     */
    @GetMapping("/routines")
    public List<Routine> getAllRoutines() {
        log.debug("REST request to get all Routines");
        return routineRepository.findAll();
    }

    /**
     * {@code GET  /routines/:id} : get the "id" routine.
     *
     * @param id the id of the routine to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the routine, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/routines/{id}")
    public ResponseEntity<Routine> getRoutine(@PathVariable String id) {
        log.debug("REST request to get Routine : {}", id);
        Optional<Routine> routine = routineRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(routine);
    }

    /**
     * {@code DELETE  /routines/:id} : delete the "id" routine.
     *
     * @param id the id of the routine to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/routines/{id}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable String id) {
        log.debug("REST request to delete Routine : {}", id);
        routineRepository.deleteById(id);
        routineSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/routines?query=:query} : search for the routine corresponding
     * to the query.
     *
     * @param query the query of the routine search.
     * @return the result of the search.
     */
    @GetMapping("/_search/routines")
    public List<Routine> searchRoutines(@RequestParam String query) {
        log.debug("REST request to search Routines for query {}", query);
        return StreamSupport
            .stream(routineSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
