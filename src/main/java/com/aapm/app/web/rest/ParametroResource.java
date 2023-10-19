package com.aapm.app.web.rest;

import com.aapm.app.repository.ParametroRepository;
import com.aapm.app.service.ParametroQueryService;
import com.aapm.app.service.ParametroService;
import com.aapm.app.service.criteria.ParametroCriteria;
import com.aapm.app.service.dto.ParametroDTO;
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
 * REST controller for managing {@link com.aapm.app.domain.Parametro}.
 */
@RestController
@RequestMapping("/api")
public class ParametroResource {

    private final Logger log = LoggerFactory.getLogger(ParametroResource.class);

    private static final String ENTITY_NAME = "parametro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParametroService parametroService;

    private final ParametroRepository parametroRepository;

    private final ParametroQueryService parametroQueryService;

    public ParametroResource(
        ParametroService parametroService,
        ParametroRepository parametroRepository,
        ParametroQueryService parametroQueryService
    ) {
        this.parametroService = parametroService;
        this.parametroRepository = parametroRepository;
        this.parametroQueryService = parametroQueryService;
    }

    /**
     * {@code POST  /parametros} : Create a new parametro.
     *
     * @param parametroDTO the parametroDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parametroDTO, or with status {@code 400 (Bad Request)} if the parametro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parametros")
    public ResponseEntity<ParametroDTO> createParametro(@Valid @RequestBody ParametroDTO parametroDTO) throws URISyntaxException {
        log.debug("REST request to save Parametro : {}", parametroDTO);
        if (parametroDTO.getId() != null) {
            throw new BadRequestAlertException("A new parametro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParametroDTO result = parametroService.save(parametroDTO);
        return ResponseEntity
            .created(new URI("/api/parametros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parametros/:id} : Updates an existing parametro.
     *
     * @param id the id of the parametroDTO to save.
     * @param parametroDTO the parametroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parametroDTO,
     * or with status {@code 400 (Bad Request)} if the parametroDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parametroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parametros/{id}")
    public ResponseEntity<ParametroDTO> updateParametro(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ParametroDTO parametroDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Parametro : {}, {}", id, parametroDTO);
        if (parametroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parametroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parametroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ParametroDTO result = parametroService.update(parametroDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parametroDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /parametros/:id} : Partial updates given fields of an existing parametro, field will ignore if it is null
     *
     * @param id the id of the parametroDTO to save.
     * @param parametroDTO the parametroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parametroDTO,
     * or with status {@code 400 (Bad Request)} if the parametroDTO is not valid,
     * or with status {@code 404 (Not Found)} if the parametroDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the parametroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parametros/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ParametroDTO> partialUpdateParametro(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ParametroDTO parametroDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Parametro partially : {}, {}", id, parametroDTO);
        if (parametroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parametroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parametroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ParametroDTO> result = parametroService.partialUpdate(parametroDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parametroDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /parametros} : get all the parametros.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parametros in body.
     */
    @GetMapping("/parametros")
    public ResponseEntity<List<ParametroDTO>> getAllParametros(
        ParametroCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Parametros by criteria: {}", criteria);
        Page<ParametroDTO> page = parametroQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /parametros/count} : count all the parametros.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/parametros/count")
    public ResponseEntity<Long> countParametros(ParametroCriteria criteria) {
        log.debug("REST request to count Parametros by criteria: {}", criteria);
        return ResponseEntity.ok().body(parametroQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /parametros/:id} : get the "id" parametro.
     *
     * @param id the id of the parametroDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parametroDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parametros/{id}")
    public ResponseEntity<ParametroDTO> getParametro(@PathVariable Long id) {
        log.debug("REST request to get Parametro : {}", id);
        Optional<ParametroDTO> parametroDTO = parametroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parametroDTO);
    }

    /**
     * {@code DELETE  /parametros/:id} : delete the "id" parametro.
     *
     * @param id the id of the parametroDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parametros/{id}")
    public ResponseEntity<Void> deleteParametro(@PathVariable Long id) {
        log.debug("REST request to delete Parametro : {}", id);
        parametroService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
