package com.aapm.app.web.rest;

import com.aapm.app.repository.DescontoConvenioRepository;
import com.aapm.app.service.DescontoConvenioQueryService;
import com.aapm.app.service.DescontoConvenioService;
import com.aapm.app.service.criteria.DescontoConvenioCriteria;
import com.aapm.app.service.dto.AssociadoDTO;
import com.aapm.app.service.dto.DescontoConvenioDTO;
import com.aapm.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.aapm.app.domain.DescontoConvenio}.
 */
@RestController
@RequestMapping("/api")
public class DescontoConvenioResource {

    private final Logger log = LoggerFactory.getLogger(DescontoConvenioResource.class);

    private static final String ENTITY_NAME = "descontoConvenio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DescontoConvenioService descontoConvenioService;

    private final DescontoConvenioRepository descontoConvenioRepository;

    private final DescontoConvenioQueryService descontoConvenioQueryService;

    public DescontoConvenioResource(
        DescontoConvenioService descontoConvenioService,
        DescontoConvenioRepository descontoConvenioRepository,
        DescontoConvenioQueryService descontoConvenioQueryService
    ) {
        this.descontoConvenioService = descontoConvenioService;
        this.descontoConvenioRepository = descontoConvenioRepository;
        this.descontoConvenioQueryService = descontoConvenioQueryService;
    }

    /**
     * {@code POST  /desconto-convenios} : Create a new descontoConvenio.
     *
     * @param descontoConvenioDTO the descontoConvenioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new descontoConvenioDTO, or with status {@code 400 (Bad Request)} if the descontoConvenio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/desconto-convenios")
    public ResponseEntity<DescontoConvenioDTO> createDescontoConvenio(@RequestBody DescontoConvenioDTO descontoConvenioDTO)
        throws URISyntaxException {
        log.debug("REST request to save DescontoConvenio : {}", descontoConvenioDTO);
        if (descontoConvenioDTO.getId() != null) {
            throw new BadRequestAlertException("A new descontoConvenio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        //        for (DescontoConvenioDTO descontoConvenioDTO1:descontoConvenioDTO){
        //            if (!descontoConvenioRepository.existsById(descontoConvenioDTO1.getId())){
        //                descontoConvenioService.save(descontoConvenioDTO1);
        //            }
        //        }
        DescontoConvenioDTO result = descontoConvenioService.save(descontoConvenioDTO);
        return ResponseEntity
            .created(new URI("/api/desconto-convenios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /desconto-convenios/:id} : Updates an existing descontoConvenio.
     *
     * @param id the id of the descontoConvenioDTO to save.
     * @param descontoConvenioDTO the descontoConvenioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated descontoConvenioDTO,
     * or with status {@code 400 (Bad Request)} if the descontoConvenioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the descontoConvenioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/desconto-convenios/{id}")
    public ResponseEntity<DescontoConvenioDTO> updateDescontoConvenio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DescontoConvenioDTO descontoConvenioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DescontoConvenio : {}, {}", id, descontoConvenioDTO);
        if (descontoConvenioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, descontoConvenioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!descontoConvenioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DescontoConvenioDTO result = descontoConvenioService.update(descontoConvenioDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, descontoConvenioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /desconto-convenios/:id} : Partial updates given fields of an existing descontoConvenio, field will ignore if it is null
     *
     * @param id the id of the descontoConvenioDTO to save.
     * @param descontoConvenioDTO the descontoConvenioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated descontoConvenioDTO,
     * or with status {@code 400 (Bad Request)} if the descontoConvenioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the descontoConvenioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the descontoConvenioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/desconto-convenios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DescontoConvenioDTO> partialUpdateDescontoConvenio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DescontoConvenioDTO descontoConvenioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DescontoConvenio partially : {}, {}", id, descontoConvenioDTO);
        if (descontoConvenioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, descontoConvenioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!descontoConvenioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DescontoConvenioDTO> result = descontoConvenioService.partialUpdate(descontoConvenioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, descontoConvenioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /desconto-convenios} : get all the descontoConvenios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of descontoConvenios in body.
     */
    @GetMapping("/desconto-convenios")
    public ResponseEntity<List<DescontoConvenioDTO>> getAllDescontoConvenios(
        DescontoConvenioCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DescontoConvenios by criteria: {}", criteria);
        Page<DescontoConvenioDTO> page = descontoConvenioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /desconto-convenios/count} : count all the descontoConvenios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/desconto-convenios/count")
    public ResponseEntity<Long> countDescontoConvenios(DescontoConvenioCriteria criteria) {
        log.debug("REST request to count DescontoConvenios by criteria: {}", criteria);
        return ResponseEntity.ok().body(descontoConvenioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /desconto-convenios/:id} : get the "id" descontoConvenio.
     *
     * @param id the id of the descontoConvenioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the descontoConvenioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/desconto-convenios/{id}")
    public ResponseEntity<DescontoConvenioDTO> getDescontoConvenio(@PathVariable Long id) {
        log.debug("REST request to get DescontoConvenio : {}", id);
        Optional<DescontoConvenioDTO> descontoConvenioDTO = descontoConvenioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(descontoConvenioDTO);
    }

    /**
     * {@code DELETE  /desconto-convenios/:id} : delete the "id" descontoConvenio.
     *
     * @param id the id of the descontoConvenioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/desconto-convenios/{id}")
    public ResponseEntity<Void> deleteDescontoConvenio(@PathVariable Long id) {
        log.debug("REST request to delete DescontoConvenio : {}", id);
        descontoConvenioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
