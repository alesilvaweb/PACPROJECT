package com.aapm.app.web.rest;

import com.aapm.app.repository.AssociadoRepository;
import com.aapm.app.service.AssociadoQueryService;
import com.aapm.app.service.AssociadoService;
import com.aapm.app.service.criteria.AssociadoCriteria;
import com.aapm.app.service.dto.AssociadoDTO;
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
 * REST controller for managing {@link com.aapm.app.domain.Associado}.
 */
@RestController
@RequestMapping("/api")
public class AssociadoResource {

    private final Logger log = LoggerFactory.getLogger(AssociadoResource.class);

    private static final String ENTITY_NAME = "associado";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssociadoService associadoService;

    private final AssociadoRepository associadoRepository;

    private final AssociadoQueryService associadoQueryService;

    public AssociadoResource(
        AssociadoService associadoService,
        AssociadoRepository associadoRepository,
        AssociadoQueryService associadoQueryService
    ) {
        this.associadoService = associadoService;
        this.associadoRepository = associadoRepository;
        this.associadoQueryService = associadoQueryService;
    }

    /**
     * {@code POST  /associados} : Create a new associado.
     *
     * @param associadoDTO the associadoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new associadoDTO, or with status {@code 400 (Bad Request)} if the associado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/associados/list")
    public ResponseEntity<AssociadoDTO> createAssociado(@Valid @RequestBody AssociadoDTO associadoDTO) throws URISyntaxException {
        log.debug("REST request to save Associado : {}", associadoDTO);
        //        if (associadoDTO.getId() != null) {
        //            throw new BadRequestAlertException("A new associado cannot already have an ID", ENTITY_NAME, "idexists");
        //        }
        AssociadoDTO result = associadoService.save(associadoDTO);
        return ResponseEntity
            .created(new URI("/api/associados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/associados")
    public ResponseEntity<AssociadoDTO> createAssociados(@Valid @RequestBody List<AssociadoDTO> associadoDTO) throws URISyntaxException {
        log.debug("REST request to save Associado : {}", associadoDTO);

        for (AssociadoDTO associadoDTO1 : associadoDTO) {
            if (!associadoRepository.existsById(associadoDTO1.getId())) {
                associadoService.save(associadoDTO1);
            }
        }

        AssociadoDTO result = new AssociadoDTO();

        return ResponseEntity
            .created(new URI("/api/associados/"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, "Lista Salva com sucesso!"))
            .body(result);
    }

    /**
     * {@code PUT  /associados/:id} : Updates an existing associado.
     *
     * @param id the id of the associadoDTO to save.
     * @param associadoDTO the associadoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated associadoDTO,
     * or with status {@code 400 (Bad Request)} if the associadoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the associadoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/associados/{id}")
    public ResponseEntity<AssociadoDTO> updateAssociado(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssociadoDTO associadoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Associado : {}, {}", id, associadoDTO);
        if (associadoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, associadoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!associadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssociadoDTO result = associadoService.update(associadoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, associadoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /associados/:id} : Partial updates given fields of an existing associado, field will ignore if it is null
     *
     * @param id the id of the associadoDTO to save.
     * @param associadoDTO the associadoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated associadoDTO,
     * or with status {@code 400 (Bad Request)} if the associadoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the associadoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the associadoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/associados/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssociadoDTO> partialUpdateAssociado(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssociadoDTO associadoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Associado partially : {}, {}", id, associadoDTO);
        if (associadoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, associadoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!associadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssociadoDTO> result = associadoService.partialUpdate(associadoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, associadoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /associados} : get all the associados.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of associados in body.
     */
    @GetMapping("/associados")
    public ResponseEntity<List<AssociadoDTO>> getAllAssociados(
        AssociadoCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Associados by criteria: {}", criteria);
        Page<AssociadoDTO> page = associadoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /associados/count} : count all the associados.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/associados/count")
    public ResponseEntity<Long> countAssociados(AssociadoCriteria criteria) {
        log.debug("REST request to count Associados by criteria: {}", criteria);
        return ResponseEntity.ok().body(associadoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /associados/:id} : get the "id" associado.
     *
     * @param id the id of the associadoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the associadoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/associados/{id}")
    public ResponseEntity<AssociadoDTO> getAssociado(@PathVariable Long id) {
        log.debug("REST request to get Associado : {}", id);
        Optional<AssociadoDTO> associadoDTO = associadoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(associadoDTO);
    }

    /**
     * {@code DELETE  /associados/:id} : delete the "id" associado.
     *
     * @param id the id of the associadoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/associados/{id}")
    public ResponseEntity<Void> deleteAssociado(@PathVariable Long id) {
        log.debug("REST request to delete Associado : {}", id);
        associadoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
