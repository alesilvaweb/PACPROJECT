package com.aapm.app.web.rest;

import com.aapm.app.repository.IconsRedesSociaisRepository;
import com.aapm.app.service.IconsRedesSociaisQueryService;
import com.aapm.app.service.IconsRedesSociaisService;
import com.aapm.app.service.criteria.IconsRedesSociaisCriteria;
import com.aapm.app.service.dto.IconsRedesSociaisDTO;
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
 * REST controller for managing {@link com.aapm.app.domain.IconsRedesSociais}.
 */
@RestController
@RequestMapping("/api")
public class IconsRedesSociaisResource {

    private final Logger log = LoggerFactory.getLogger(IconsRedesSociaisResource.class);

    private static final String ENTITY_NAME = "iconsRedesSociais";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IconsRedesSociaisService iconsRedesSociaisService;

    private final IconsRedesSociaisRepository iconsRedesSociaisRepository;

    private final IconsRedesSociaisQueryService iconsRedesSociaisQueryService;

    public IconsRedesSociaisResource(
        IconsRedesSociaisService iconsRedesSociaisService,
        IconsRedesSociaisRepository iconsRedesSociaisRepository,
        IconsRedesSociaisQueryService iconsRedesSociaisQueryService
    ) {
        this.iconsRedesSociaisService = iconsRedesSociaisService;
        this.iconsRedesSociaisRepository = iconsRedesSociaisRepository;
        this.iconsRedesSociaisQueryService = iconsRedesSociaisQueryService;
    }

    /**
     * {@code POST  /icons-redes-sociais} : Create a new iconsRedesSociais.
     *
     * @param iconsRedesSociaisDTO the iconsRedesSociaisDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iconsRedesSociaisDTO, or with status {@code 400 (Bad Request)} if the iconsRedesSociais has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/icons-redes-sociais")
    public ResponseEntity<IconsRedesSociaisDTO> createIconsRedesSociais(@Valid @RequestBody IconsRedesSociaisDTO iconsRedesSociaisDTO)
        throws URISyntaxException {
        log.debug("REST request to save IconsRedesSociais : {}", iconsRedesSociaisDTO);
        if (iconsRedesSociaisDTO.getId() != null) {
            throw new BadRequestAlertException("A new iconsRedesSociais cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IconsRedesSociaisDTO result = iconsRedesSociaisService.save(iconsRedesSociaisDTO);
        return ResponseEntity
            .created(new URI("/api/icons-redes-sociais/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /icons-redes-sociais/:id} : Updates an existing iconsRedesSociais.
     *
     * @param id the id of the iconsRedesSociaisDTO to save.
     * @param iconsRedesSociaisDTO the iconsRedesSociaisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iconsRedesSociaisDTO,
     * or with status {@code 400 (Bad Request)} if the iconsRedesSociaisDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iconsRedesSociaisDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/icons-redes-sociais/{id}")
    public ResponseEntity<IconsRedesSociaisDTO> updateIconsRedesSociais(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IconsRedesSociaisDTO iconsRedesSociaisDTO
    ) throws URISyntaxException {
        log.debug("REST request to update IconsRedesSociais : {}, {}", id, iconsRedesSociaisDTO);
        if (iconsRedesSociaisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iconsRedesSociaisDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!iconsRedesSociaisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IconsRedesSociaisDTO result = iconsRedesSociaisService.update(iconsRedesSociaisDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, iconsRedesSociaisDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /icons-redes-sociais/:id} : Partial updates given fields of an existing iconsRedesSociais, field will ignore if it is null
     *
     * @param id the id of the iconsRedesSociaisDTO to save.
     * @param iconsRedesSociaisDTO the iconsRedesSociaisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iconsRedesSociaisDTO,
     * or with status {@code 400 (Bad Request)} if the iconsRedesSociaisDTO is not valid,
     * or with status {@code 404 (Not Found)} if the iconsRedesSociaisDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the iconsRedesSociaisDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/icons-redes-sociais/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IconsRedesSociaisDTO> partialUpdateIconsRedesSociais(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IconsRedesSociaisDTO iconsRedesSociaisDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update IconsRedesSociais partially : {}, {}", id, iconsRedesSociaisDTO);
        if (iconsRedesSociaisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iconsRedesSociaisDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!iconsRedesSociaisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IconsRedesSociaisDTO> result = iconsRedesSociaisService.partialUpdate(iconsRedesSociaisDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, iconsRedesSociaisDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /icons-redes-sociais} : get all the iconsRedesSociais.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of iconsRedesSociais in body.
     */
    @GetMapping("/icons-redes-sociais")
    public ResponseEntity<List<IconsRedesSociaisDTO>> getAllIconsRedesSociais(
        IconsRedesSociaisCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get IconsRedesSociais by criteria: {}", criteria);
        Page<IconsRedesSociaisDTO> page = iconsRedesSociaisQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /icons-redes-sociais/count} : count all the iconsRedesSociais.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/icons-redes-sociais/count")
    public ResponseEntity<Long> countIconsRedesSociais(IconsRedesSociaisCriteria criteria) {
        log.debug("REST request to count IconsRedesSociais by criteria: {}", criteria);
        return ResponseEntity.ok().body(iconsRedesSociaisQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /icons-redes-sociais/:id} : get the "id" iconsRedesSociais.
     *
     * @param id the id of the iconsRedesSociaisDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iconsRedesSociaisDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/icons-redes-sociais/{id}")
    public ResponseEntity<IconsRedesSociaisDTO> getIconsRedesSociais(@PathVariable Long id) {
        log.debug("REST request to get IconsRedesSociais : {}", id);
        Optional<IconsRedesSociaisDTO> iconsRedesSociaisDTO = iconsRedesSociaisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(iconsRedesSociaisDTO);
    }

    /**
     * {@code DELETE  /icons-redes-sociais/:id} : delete the "id" iconsRedesSociais.
     *
     * @param id the id of the iconsRedesSociaisDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/icons-redes-sociais/{id}")
    public ResponseEntity<Void> deleteIconsRedesSociais(@PathVariable Long id) {
        log.debug("REST request to delete IconsRedesSociais : {}", id);
        iconsRedesSociaisService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
