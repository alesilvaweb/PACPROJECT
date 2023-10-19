package com.aapm.app.web.rest;

import com.aapm.app.repository.RedesSociaisConvenioRepository;
import com.aapm.app.service.RedesSociaisConvenioQueryService;
import com.aapm.app.service.RedesSociaisConvenioService;
import com.aapm.app.service.criteria.RedesSociaisConvenioCriteria;
import com.aapm.app.service.dto.RedesSociaisConvenioDTO;
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
 * REST controller for managing {@link com.aapm.app.domain.RedesSociaisConvenio}.
 */
@RestController
@RequestMapping("/api")
public class RedesSociaisConvenioResource {

    private final Logger log = LoggerFactory.getLogger(RedesSociaisConvenioResource.class);

    private static final String ENTITY_NAME = "redesSociaisConvenio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RedesSociaisConvenioService redesSociaisConvenioService;

    private final RedesSociaisConvenioRepository redesSociaisConvenioRepository;

    private final RedesSociaisConvenioQueryService redesSociaisConvenioQueryService;

    public RedesSociaisConvenioResource(
        RedesSociaisConvenioService redesSociaisConvenioService,
        RedesSociaisConvenioRepository redesSociaisConvenioRepository,
        RedesSociaisConvenioQueryService redesSociaisConvenioQueryService
    ) {
        this.redesSociaisConvenioService = redesSociaisConvenioService;
        this.redesSociaisConvenioRepository = redesSociaisConvenioRepository;
        this.redesSociaisConvenioQueryService = redesSociaisConvenioQueryService;
    }

    /**
     * {@code POST  /redes-sociais-convenios} : Create a new redesSociaisConvenio.
     *
     * @param redesSociaisConvenioDTO the redesSociaisConvenioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new redesSociaisConvenioDTO, or with status {@code 400 (Bad Request)} if the redesSociaisConvenio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/redes-sociais-convenios")
    public ResponseEntity<RedesSociaisConvenioDTO> createRedesSociaisConvenio(
        @Valid @RequestBody RedesSociaisConvenioDTO redesSociaisConvenioDTO
    ) throws URISyntaxException {
        log.debug("REST request to save RedesSociaisConvenio : {}", redesSociaisConvenioDTO);
        if (redesSociaisConvenioDTO.getId() != null) {
            throw new BadRequestAlertException("A new redesSociaisConvenio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RedesSociaisConvenioDTO result = redesSociaisConvenioService.save(redesSociaisConvenioDTO);
        return ResponseEntity
            .created(new URI("/api/redes-sociais-convenios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /redes-sociais-convenios/:id} : Updates an existing redesSociaisConvenio.
     *
     * @param id the id of the redesSociaisConvenioDTO to save.
     * @param redesSociaisConvenioDTO the redesSociaisConvenioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated redesSociaisConvenioDTO,
     * or with status {@code 400 (Bad Request)} if the redesSociaisConvenioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the redesSociaisConvenioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/redes-sociais-convenios/{id}")
    public ResponseEntity<RedesSociaisConvenioDTO> updateRedesSociaisConvenio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RedesSociaisConvenioDTO redesSociaisConvenioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RedesSociaisConvenio : {}, {}", id, redesSociaisConvenioDTO);
        if (redesSociaisConvenioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, redesSociaisConvenioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!redesSociaisConvenioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RedesSociaisConvenioDTO result = redesSociaisConvenioService.update(redesSociaisConvenioDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, redesSociaisConvenioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /redes-sociais-convenios/:id} : Partial updates given fields of an existing redesSociaisConvenio, field will ignore if it is null
     *
     * @param id the id of the redesSociaisConvenioDTO to save.
     * @param redesSociaisConvenioDTO the redesSociaisConvenioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated redesSociaisConvenioDTO,
     * or with status {@code 400 (Bad Request)} if the redesSociaisConvenioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the redesSociaisConvenioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the redesSociaisConvenioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/redes-sociais-convenios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RedesSociaisConvenioDTO> partialUpdateRedesSociaisConvenio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RedesSociaisConvenioDTO redesSociaisConvenioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RedesSociaisConvenio partially : {}, {}", id, redesSociaisConvenioDTO);
        if (redesSociaisConvenioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, redesSociaisConvenioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!redesSociaisConvenioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RedesSociaisConvenioDTO> result = redesSociaisConvenioService.partialUpdate(redesSociaisConvenioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, redesSociaisConvenioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /redes-sociais-convenios} : get all the redesSociaisConvenios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of redesSociaisConvenios in body.
     */
    @GetMapping("/redes-sociais-convenios")
    public ResponseEntity<List<RedesSociaisConvenioDTO>> getAllRedesSociaisConvenios(
        RedesSociaisConvenioCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get RedesSociaisConvenios by criteria: {}", criteria);
        Page<RedesSociaisConvenioDTO> page = redesSociaisConvenioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /redes-sociais-convenios/count} : count all the redesSociaisConvenios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/redes-sociais-convenios/count")
    public ResponseEntity<Long> countRedesSociaisConvenios(RedesSociaisConvenioCriteria criteria) {
        log.debug("REST request to count RedesSociaisConvenios by criteria: {}", criteria);
        return ResponseEntity.ok().body(redesSociaisConvenioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /redes-sociais-convenios/:id} : get the "id" redesSociaisConvenio.
     *
     * @param id the id of the redesSociaisConvenioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the redesSociaisConvenioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/redes-sociais-convenios/{id}")
    public ResponseEntity<RedesSociaisConvenioDTO> getRedesSociaisConvenio(@PathVariable Long id) {
        log.debug("REST request to get RedesSociaisConvenio : {}", id);
        Optional<RedesSociaisConvenioDTO> redesSociaisConvenioDTO = redesSociaisConvenioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(redesSociaisConvenioDTO);
    }

    /**
     * {@code DELETE  /redes-sociais-convenios/:id} : delete the "id" redesSociaisConvenio.
     *
     * @param id the id of the redesSociaisConvenioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/redes-sociais-convenios/{id}")
    public ResponseEntity<Void> deleteRedesSociaisConvenio(@PathVariable Long id) {
        log.debug("REST request to delete RedesSociaisConvenio : {}", id);
        redesSociaisConvenioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
