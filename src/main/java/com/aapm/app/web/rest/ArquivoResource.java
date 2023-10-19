package com.aapm.app.web.rest;

import com.aapm.app.repository.ArquivoRepository;
import com.aapm.app.service.ArquivoQueryService;
import com.aapm.app.service.ArquivoService;
import com.aapm.app.service.criteria.ArquivoCriteria;
import com.aapm.app.service.dto.ArquivoDTO;
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
 * REST controller for managing {@link com.aapm.app.domain.Arquivo}.
 */
@RestController
@RequestMapping("/api")
public class ArquivoResource {

    private final Logger log = LoggerFactory.getLogger(ArquivoResource.class);

    private static final String ENTITY_NAME = "arquivo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArquivoService arquivoService;

    private final ArquivoRepository arquivoRepository;

    private final ArquivoQueryService arquivoQueryService;

    public ArquivoResource(ArquivoService arquivoService, ArquivoRepository arquivoRepository, ArquivoQueryService arquivoQueryService) {
        this.arquivoService = arquivoService;
        this.arquivoRepository = arquivoRepository;
        this.arquivoQueryService = arquivoQueryService;
    }

    /**
     * {@code POST  /arquivos} : Create a new arquivo.
     *
     * @param arquivoDTO the arquivoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new arquivoDTO, or with status {@code 400 (Bad Request)} if the arquivo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/arquivos")
    public ResponseEntity<ArquivoDTO> createArquivo(@Valid @RequestBody ArquivoDTO arquivoDTO) throws URISyntaxException {
        log.debug("REST request to save Arquivo : {}", arquivoDTO);
        if (arquivoDTO.getId() != null) {
            throw new BadRequestAlertException("A new arquivo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArquivoDTO result = arquivoService.save(arquivoDTO);
        return ResponseEntity
            .created(new URI("/api/arquivos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /arquivos/:id} : Updates an existing arquivo.
     *
     * @param id the id of the arquivoDTO to save.
     * @param arquivoDTO the arquivoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arquivoDTO,
     * or with status {@code 400 (Bad Request)} if the arquivoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the arquivoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/arquivos/{id}")
    public ResponseEntity<ArquivoDTO> updateArquivo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArquivoDTO arquivoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Arquivo : {}, {}", id, arquivoDTO);
        if (arquivoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, arquivoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!arquivoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ArquivoDTO result = arquivoService.update(arquivoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arquivoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /arquivos/:id} : Partial updates given fields of an existing arquivo, field will ignore if it is null
     *
     * @param id the id of the arquivoDTO to save.
     * @param arquivoDTO the arquivoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arquivoDTO,
     * or with status {@code 400 (Bad Request)} if the arquivoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the arquivoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the arquivoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/arquivos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArquivoDTO> partialUpdateArquivo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ArquivoDTO arquivoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Arquivo partially : {}, {}", id, arquivoDTO);
        if (arquivoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, arquivoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!arquivoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArquivoDTO> result = arquivoService.partialUpdate(arquivoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arquivoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /arquivos} : get all the arquivos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of arquivos in body.
     */
    @GetMapping("/arquivos")
    public ResponseEntity<List<ArquivoDTO>> getAllArquivos(
        ArquivoCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Arquivos by criteria: {}", criteria);
        Page<ArquivoDTO> page = arquivoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /arquivos/count} : count all the arquivos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/arquivos/count")
    public ResponseEntity<Long> countArquivos(ArquivoCriteria criteria) {
        log.debug("REST request to count Arquivos by criteria: {}", criteria);
        return ResponseEntity.ok().body(arquivoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /arquivos/:id} : get the "id" arquivo.
     *
     * @param id the id of the arquivoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the arquivoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/arquivos/{id}")
    public ResponseEntity<ArquivoDTO> getArquivo(@PathVariable Long id) {
        log.debug("REST request to get Arquivo : {}", id);
        Optional<ArquivoDTO> arquivoDTO = arquivoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(arquivoDTO);
    }

    /**
     * {@code DELETE  /arquivos/:id} : delete the "id" arquivo.
     *
     * @param id the id of the arquivoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/arquivos/{id}")
    public ResponseEntity<Void> deleteArquivo(@PathVariable Long id) {
        log.debug("REST request to delete Arquivo : {}", id);
        arquivoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
