package com.pandatech.pandafit.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.pandatech.pandafit.domain.Cycle;
import com.pandatech.pandafit.repository.CycleRepository;
import com.pandatech.pandafit.repository.search.CycleSearchRepository;
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
 * REST controller for managing {@link com.pandatech.pandafit.domain.Cycle}.
 */
@RestController
@RequestMapping("/api")
public class CycleResource {

    private final Logger log = LoggerFactory.getLogger(CycleResource.class);

    private static final String ENTITY_NAME = "cycle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CycleRepository cycleRepository;

    private final CycleSearchRepository cycleSearchRepository;

    public CycleResource(CycleRepository cycleRepository, CycleSearchRepository cycleSearchRepository) {
        this.cycleRepository = cycleRepository;
        this.cycleSearchRepository = cycleSearchRepository;
    }

    /**
     * {@code POST  /cycles} : Create a new cycle.
     *
     * @param cycle the cycle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cycle, or with status {@code 400 (Bad Request)} if the cycle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cycles")
    public ResponseEntity<Cycle> createCycle(@RequestBody Cycle cycle) throws URISyntaxException {
        log.debug("REST request to save Cycle : {}", cycle);
        if (cycle.getId() != null) {
            throw new BadRequestAlertException("A new cycle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cycle result = cycleRepository.save(cycle);
        cycleSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/cycles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /cycles/:id} : Updates an existing cycle.
     *
     * @param id the id of the cycle to save.
     * @param cycle the cycle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cycle,
     * or with status {@code 400 (Bad Request)} if the cycle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cycle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cycles/{id}")
    public ResponseEntity<Cycle> updateCycle(@PathVariable(value = "id", required = false) final String id, @RequestBody Cycle cycle)
        throws URISyntaxException {
        log.debug("REST request to update Cycle : {}, {}", id, cycle);
        if (cycle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cycle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cycleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cycle result = cycleRepository.save(cycle);
        cycleSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cycle.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /cycles/:id} : Partial updates given fields of an existing cycle, field will ignore if it is null
     *
     * @param id the id of the cycle to save.
     * @param cycle the cycle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cycle,
     * or with status {@code 400 (Bad Request)} if the cycle is not valid,
     * or with status {@code 404 (Not Found)} if the cycle is not found,
     * or with status {@code 500 (Internal Server Error)} if the cycle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cycles/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Cycle> partialUpdateCycle(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Cycle cycle
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cycle partially : {}, {}", id, cycle);
        if (cycle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cycle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cycleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cycle> result = cycleRepository
            .findById(cycle.getId())
            .map(
                existingCycle -> {
                    if (cycle.getReps() != null) {
                        existingCycle.setReps(cycle.getReps());
                    }
                    if (cycle.getVolume() != null) {
                        existingCycle.setVolume(cycle.getVolume());
                    }

                    return existingCycle;
                }
            )
            .map(cycleRepository::save)
            .map(
                savedCycle -> {
                    cycleSearchRepository.save(savedCycle);

                    return savedCycle;
                }
            );

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cycle.getId()));
    }

    /**
     * {@code GET  /cycles} : get all the cycles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cycles in body.
     */
    @GetMapping("/cycles")
    public List<Cycle> getAllCycles() {
        log.debug("REST request to get all Cycles");
        return cycleRepository.findAll();
    }

    /**
     * {@code GET  /cycles/:id} : get the "id" cycle.
     *
     * @param id the id of the cycle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cycle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cycles/{id}")
    public ResponseEntity<Cycle> getCycle(@PathVariable String id) {
        log.debug("REST request to get Cycle : {}", id);
        Optional<Cycle> cycle = cycleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cycle);
    }

    /**
     * {@code DELETE  /cycles/:id} : delete the "id" cycle.
     *
     * @param id the id of the cycle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cycles/{id}")
    public ResponseEntity<Void> deleteCycle(@PathVariable String id) {
        log.debug("REST request to delete Cycle : {}", id);
        cycleRepository.deleteById(id);
        cycleSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/cycles?query=:query} : search for the cycle corresponding
     * to the query.
     *
     * @param query the query of the cycle search.
     * @return the result of the search.
     */
    @GetMapping("/_search/cycles")
    public List<Cycle> searchCycles(@RequestParam String query) {
        log.debug("REST request to search Cycles for query {}", query);
        return StreamSupport
            .stream(cycleSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
