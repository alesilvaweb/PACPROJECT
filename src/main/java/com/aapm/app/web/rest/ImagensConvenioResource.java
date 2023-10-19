package com.aapm.app.web.rest;

import com.aapm.app.repository.ImagensConvenioRepository;
import com.aapm.app.service.ImagensConvenioQueryService;
import com.aapm.app.service.ImagensConvenioService;
import com.aapm.app.service.criteria.ImagensConvenioCriteria;
import com.aapm.app.service.dto.ImagensConvenioDTO;
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
 * REST controller for managing {@link com.aapm.app.domain.ImagensConvenio}.
 */
@RestController
@RequestMapping("/api")
public class ImagensConvenioResource {

    private final Logger log = LoggerFactory.getLogger(ImagensConvenioResource.class);

    private static final String ENTITY_NAME = "imagensConvenio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImagensConvenioService imagensConvenioService;

    private final ImagensConvenioRepository imagensConvenioRepository;

    private final ImagensConvenioQueryService imagensConvenioQueryService;

    public ImagensConvenioResource(
        ImagensConvenioService imagensConvenioService,
        ImagensConvenioRepository imagensConvenioRepository,
        ImagensConvenioQueryService imagensConvenioQueryService
    ) {
        this.imagensConvenioService = imagensConvenioService;
        this.imagensConvenioRepository = imagensConvenioRepository;
        this.imagensConvenioQueryService = imagensConvenioQueryService;
    }

    /**
     * {@code POST  /imagens-convenios} : Create a new imagensConvenio.
     *
     * @param imagensConvenioDTO the imagensConvenioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imagensConvenioDTO, or with status {@code 400 (Bad Request)} if the imagensConvenio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/imagens-convenios")
    public ResponseEntity<ImagensConvenioDTO> createImagensConvenio(@Valid @RequestBody ImagensConvenioDTO imagensConvenioDTO)
        throws URISyntaxException {
        log.debug("REST request to save ImagensConvenio : {}", imagensConvenioDTO);
        if (imagensConvenioDTO.getId() != null) {
            throw new BadRequestAlertException("A new imagensConvenio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ImagensConvenioDTO result = imagensConvenioService.save(imagensConvenioDTO);
        return ResponseEntity
            .created(new URI("/api/imagens-convenios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /imagens-convenios/:id} : Updates an existing imagensConvenio.
     *
     * @param id the id of the imagensConvenioDTO to save.
     * @param imagensConvenioDTO the imagensConvenioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imagensConvenioDTO,
     * or with status {@code 400 (Bad Request)} if the imagensConvenioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imagensConvenioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/imagens-convenios/{id}")
    public ResponseEntity<ImagensConvenioDTO> updateImagensConvenio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ImagensConvenioDTO imagensConvenioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ImagensConvenio : {}, {}", id, imagensConvenioDTO);
        if (imagensConvenioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imagensConvenioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imagensConvenioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ImagensConvenioDTO result = imagensConvenioService.update(imagensConvenioDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imagensConvenioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /imagens-convenios/:id} : Partial updates given fields of an existing imagensConvenio, field will ignore if it is null
     *
     * @param id the id of the imagensConvenioDTO to save.
     * @param imagensConvenioDTO the imagensConvenioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imagensConvenioDTO,
     * or with status {@code 400 (Bad Request)} if the imagensConvenioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the imagensConvenioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the imagensConvenioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/imagens-convenios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImagensConvenioDTO> partialUpdateImagensConvenio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ImagensConvenioDTO imagensConvenioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ImagensConvenio partially : {}, {}", id, imagensConvenioDTO);
        if (imagensConvenioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imagensConvenioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imagensConvenioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImagensConvenioDTO> result = imagensConvenioService.partialUpdate(imagensConvenioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imagensConvenioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /imagens-convenios} : get all the imagensConvenios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of imagensConvenios in body.
     */
    @GetMapping("/imagens-convenios")
    public ResponseEntity<List<ImagensConvenioDTO>> getAllImagensConvenios(
        ImagensConvenioCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ImagensConvenios by criteria: {}", criteria);
        Page<ImagensConvenioDTO> page = imagensConvenioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /imagens-convenios/count} : count all the imagensConvenios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/imagens-convenios/count")
    public ResponseEntity<Long> countImagensConvenios(ImagensConvenioCriteria criteria) {
        log.debug("REST request to count ImagensConvenios by criteria: {}", criteria);
        return ResponseEntity.ok().body(imagensConvenioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /imagens-convenios/:id} : get the "id" imagensConvenio.
     *
     * @param id the id of the imagensConvenioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imagensConvenioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/imagens-convenios/{id}")
    public ResponseEntity<ImagensConvenioDTO> getImagensConvenio(@PathVariable Long id) {
        log.debug("REST request to get ImagensConvenio : {}", id);
        Optional<ImagensConvenioDTO> imagensConvenioDTO = imagensConvenioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(imagensConvenioDTO);
    }

    /**
     * {@code DELETE  /imagens-convenios/:id} : delete the "id" imagensConvenio.
     *
     * @param id the id of the imagensConvenioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/imagens-convenios/{id}")
    public ResponseEntity<Void> deleteImagensConvenio(@PathVariable Long id) {
        log.debug("REST request to delete ImagensConvenio : {}", id);
        imagensConvenioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
