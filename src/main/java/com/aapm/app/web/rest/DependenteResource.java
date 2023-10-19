package com.aapm.app.web.rest;

import com.aapm.app.repository.DependenteRepository;
import com.aapm.app.service.DependenteQueryService;
import com.aapm.app.service.DependenteService;
import com.aapm.app.service.criteria.DependenteCriteria;
import com.aapm.app.service.dto.DependenteDTO;
import com.aapm.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.aapm.app.domain.Dependente}.
 */
@RestController
@RequestMapping("/api")
public class DependenteResource {

    private final Logger log = LoggerFactory.getLogger(DependenteResource.class);

    private static final String ENTITY_NAME = "dependente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DependenteService dependenteService;

    private final DependenteRepository dependenteRepository;

    private final DependenteQueryService dependenteQueryService;

    public DependenteResource(
        DependenteService dependenteService,
        DependenteRepository dependenteRepository,
        DependenteQueryService dependenteQueryService
    ) {
        this.dependenteService = dependenteService;
        this.dependenteRepository = dependenteRepository;
        this.dependenteQueryService = dependenteQueryService;
    }

    /**
     * {@code POST  /dependentes} : Create a new dependente.
     *
     * @param dependenteDTO the dependenteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dependenteDTO, or with status {@code 400 (Bad Request)} if the dependente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dependentes")
    public ResponseEntity<DependenteDTO> createDependente(@Valid @RequestBody DependenteDTO dependenteDTO) throws URISyntaxException {
        log.debug("REST request to save Dependente : {}", dependenteDTO);
        if (dependenteDTO.getId() != null) {
            throw new BadRequestAlertException("A new dependente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DependenteDTO result = dependenteService.save(dependenteDTO);
        return ResponseEntity
            .created(new URI("/api/dependentes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dependentes/:id} : Updates an existing dependente.
     *
     * @param id the id of the dependenteDTO to save.
     * @param dependenteDTO the dependenteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dependenteDTO,
     * or with status {@code 400 (Bad Request)} if the dependenteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dependenteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dependentes/{id}")
    public ResponseEntity<DependenteDTO> updateDependente(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DependenteDTO dependenteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Dependente : {}, {}", id, dependenteDTO);
        if (dependenteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dependenteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dependenteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DependenteDTO result = dependenteService.update(dependenteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dependenteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dependentes/:id} : Partial updates given fields of an existing dependente, field will ignore if it is null
     *
     * @param id the id of the dependenteDTO to save.
     * @param dependenteDTO the dependenteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dependenteDTO,
     * or with status {@code 400 (Bad Request)} if the dependenteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dependenteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dependenteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dependentes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DependenteDTO> partialUpdateDependente(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DependenteDTO dependenteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dependente partially : {}, {}", id, dependenteDTO);
        if (dependenteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dependenteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dependenteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DependenteDTO> result = dependenteService.partialUpdate(dependenteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dependenteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dependentes} : get all the dependentes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dependentes in body.
     */
    @GetMapping("/dependentes")
    public ResponseEntity<List<DependenteDTO>> getAllDependentes(
        DependenteCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Dependentes by criteria: {}", criteria);
        Page<DependenteDTO> page = dependenteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dependentes/count} : count all the dependentes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/dependentes/count")
    public ResponseEntity<Long> countDependentes(DependenteCriteria criteria) {
        log.debug("REST request to count Dependentes by criteria: {}", criteria);
        return ResponseEntity.ok().body(dependenteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /dependentes/:id} : get the "id" dependente.
     *
     * @param id the id of the dependenteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dependenteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dependentes/{id}")
    public ResponseEntity<DependenteDTO> getDependente(@PathVariable Long id) {
        log.debug("REST request to get Dependente : {}", id);
        Optional<DependenteDTO> dependenteDTO = dependenteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dependenteDTO);
    }

    /**
     * {@code DELETE  /dependentes/:id} : delete the "id" dependente.
     *
     * @param id the id of the dependenteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dependentes/{id}")
    public ResponseEntity<Void> deleteDependente(@PathVariable Long id) {
        log.debug("REST request to delete Dependente : {}", id);
        dependenteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
