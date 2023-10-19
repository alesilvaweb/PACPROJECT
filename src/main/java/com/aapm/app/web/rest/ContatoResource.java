package com.aapm.app.web.rest;

import com.aapm.app.repository.ContatoRepository;
import com.aapm.app.service.ContatoQueryService;
import com.aapm.app.service.ContatoService;
import com.aapm.app.service.criteria.ContatoCriteria;
import com.aapm.app.service.dto.ContatoDTO;
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
 * REST controller for managing {@link com.aapm.app.domain.Contato}.
 */
@RestController
@RequestMapping("/api")
public class ContatoResource {

    private final Logger log = LoggerFactory.getLogger(ContatoResource.class);

    private static final String ENTITY_NAME = "contato";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContatoService contatoService;

    private final ContatoRepository contatoRepository;

    private final ContatoQueryService contatoQueryService;

    public ContatoResource(ContatoService contatoService, ContatoRepository contatoRepository, ContatoQueryService contatoQueryService) {
        this.contatoService = contatoService;
        this.contatoRepository = contatoRepository;
        this.contatoQueryService = contatoQueryService;
    }

    /**
     * {@code POST  /contatoes} : Create a new contato.
     *
     * @param contatoDTO the contatoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contatoDTO, or with status {@code 400 (Bad Request)} if the contato has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contatoes")
    public ResponseEntity<ContatoDTO> createContato(@Valid @RequestBody ContatoDTO contatoDTO) throws URISyntaxException {
        log.debug("REST request to save Contato : {}", contatoDTO);
        if (contatoDTO.getId() != null) {
            throw new BadRequestAlertException("A new contato cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContatoDTO result = contatoService.save(contatoDTO);
        return ResponseEntity
            .created(new URI("/api/contatoes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contatoes/:id} : Updates an existing contato.
     *
     * @param id the id of the contatoDTO to save.
     * @param contatoDTO the contatoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contatoDTO,
     * or with status {@code 400 (Bad Request)} if the contatoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contatoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contatoes/{id}")
    public ResponseEntity<ContatoDTO> updateContato(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContatoDTO contatoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Contato : {}, {}", id, contatoDTO);
        if (contatoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contatoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contatoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContatoDTO result = contatoService.update(contatoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contatoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contatoes/:id} : Partial updates given fields of an existing contato, field will ignore if it is null
     *
     * @param id the id of the contatoDTO to save.
     * @param contatoDTO the contatoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contatoDTO,
     * or with status {@code 400 (Bad Request)} if the contatoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contatoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contatoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contatoes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContatoDTO> partialUpdateContato(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContatoDTO contatoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Contato partially : {}, {}", id, contatoDTO);
        if (contatoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contatoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contatoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContatoDTO> result = contatoService.partialUpdate(contatoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contatoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contatoes} : get all the contatoes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contatoes in body.
     */
    @GetMapping("/contatoes")
    public ResponseEntity<List<ContatoDTO>> getAllContatoes(
        ContatoCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Contatoes by criteria: {}", criteria);
        Page<ContatoDTO> page = contatoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contatoes/count} : count all the contatoes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/contatoes/count")
    public ResponseEntity<Long> countContatoes(ContatoCriteria criteria) {
        log.debug("REST request to count Contatoes by criteria: {}", criteria);
        return ResponseEntity.ok().body(contatoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /contatoes/:id} : get the "id" contato.
     *
     * @param id the id of the contatoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contatoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contatoes/{id}")
    public ResponseEntity<ContatoDTO> getContato(@PathVariable Long id) {
        log.debug("REST request to get Contato : {}", id);
        Optional<ContatoDTO> contatoDTO = contatoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contatoDTO);
    }

    /**
     * {@code DELETE  /contatoes/:id} : delete the "id" contato.
     *
     * @param id the id of the contatoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contatoes/{id}")
    public ResponseEntity<Void> deleteContato(@PathVariable Long id) {
        log.debug("REST request to delete Contato : {}", id);
        contatoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
