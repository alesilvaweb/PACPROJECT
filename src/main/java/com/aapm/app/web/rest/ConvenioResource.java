package com.aapm.app.web.rest;

import com.aapm.app.repository.ConvenioRepository;
import com.aapm.app.service.ConvenioQueryService;
import com.aapm.app.service.ConvenioService;
import com.aapm.app.service.criteria.ConvenioCriteria;
import com.aapm.app.service.dto.ConvenioDTO;
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
 * REST controller for managing {@link com.aapm.app.domain.Convenio}.
 */
@RestController
@RequestMapping("/api")
public class ConvenioResource {

    private final Logger log = LoggerFactory.getLogger(ConvenioResource.class);

    private static final String ENTITY_NAME = "convenio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConvenioService convenioService;

    private final ConvenioRepository convenioRepository;

    private final ConvenioQueryService convenioQueryService;

    public ConvenioResource(
        ConvenioService convenioService,
        ConvenioRepository convenioRepository,
        ConvenioQueryService convenioQueryService
    ) {
        this.convenioService = convenioService;
        this.convenioRepository = convenioRepository;
        this.convenioQueryService = convenioQueryService;
    }

    /**
     * {@code POST  /convenios} : Create a new convenio.
     *
     * @param convenioDTO the convenioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new convenioDTO, or with status {@code 400 (Bad Request)} if the convenio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/convenios")
    public ResponseEntity<ConvenioDTO> createConvenio(@Valid @RequestBody ConvenioDTO convenioDTO) throws URISyntaxException {
        log.debug("REST request to save Convenio : {}", convenioDTO);
        if (convenioDTO.getId() != null) {
            throw new BadRequestAlertException("A new convenio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConvenioDTO result = convenioService.save(convenioDTO);
        return ResponseEntity
            .created(new URI("/api/convenios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /convenios/:id} : Updates an existing convenio.
     *
     * @param id the id of the convenioDTO to save.
     * @param convenioDTO the convenioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated convenioDTO,
     * or with status {@code 400 (Bad Request)} if the convenioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the convenioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/convenios/{id}")
    public ResponseEntity<ConvenioDTO> updateConvenio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ConvenioDTO convenioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Convenio : {}, {}", id, convenioDTO);
        if (convenioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, convenioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!convenioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ConvenioDTO result = convenioService.update(convenioDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, convenioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /convenios/:id} : Partial updates given fields of an existing convenio, field will ignore if it is null
     *
     * @param id the id of the convenioDTO to save.
     * @param convenioDTO the convenioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated convenioDTO,
     * or with status {@code 400 (Bad Request)} if the convenioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the convenioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the convenioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/convenios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConvenioDTO> partialUpdateConvenio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ConvenioDTO convenioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Convenio partially : {}, {}", id, convenioDTO);
        if (convenioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, convenioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!convenioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConvenioDTO> result = convenioService.partialUpdate(convenioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, convenioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /convenios} : get all the convenios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of convenios in body.
     */
    @GetMapping("/convenios")
    public ResponseEntity<List<ConvenioDTO>> getAllConvenios(
        ConvenioCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Convenios by criteria: {}", criteria);
        Page<ConvenioDTO> page = convenioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /convenios/count} : count all the convenios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/convenios/count")
    public ResponseEntity<Long> countConvenios(ConvenioCriteria criteria) {
        log.debug("REST request to count Convenios by criteria: {}", criteria);
        return ResponseEntity.ok().body(convenioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /convenios/:id} : get the "id" convenio.
     *
     * @param id the id of the convenioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the convenioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/convenios/{id}")
    public ResponseEntity<ConvenioDTO> getConvenio(@PathVariable Long id) {
        log.debug("REST request to get Convenio : {}", id);
        Optional<ConvenioDTO> convenioDTO = convenioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(convenioDTO);
    }

    /**
     * {@code DELETE  /convenios/:id} : delete the "id" convenio.
     *
     * @param id the id of the convenioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/convenios/{id}")
    public ResponseEntity<Void> deleteConvenio(@PathVariable Long id) {
        log.debug("REST request to delete Convenio : {}", id);
        convenioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
