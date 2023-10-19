package com.aapm.app.web.rest;

import com.aapm.app.repository.MensagemRepository;
import com.aapm.app.service.MensagemQueryService;
import com.aapm.app.service.MensagemService;
import com.aapm.app.service.criteria.MensagemCriteria;
import com.aapm.app.service.dto.MensagemDTO;
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
 * REST controller for managing {@link com.aapm.app.domain.Mensagem}.
 */
@RestController
@RequestMapping("/api")
public class MensagemResource {

    private final Logger log = LoggerFactory.getLogger(MensagemResource.class);

    private static final String ENTITY_NAME = "mensagem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MensagemService mensagemService;

    private final MensagemRepository mensagemRepository;

    private final MensagemQueryService mensagemQueryService;

    public MensagemResource(
        MensagemService mensagemService,
        MensagemRepository mensagemRepository,
        MensagemQueryService mensagemQueryService
    ) {
        this.mensagemService = mensagemService;
        this.mensagemRepository = mensagemRepository;
        this.mensagemQueryService = mensagemQueryService;
    }

    /**
     * {@code POST  /mensagems} : Create a new mensagem.
     *
     * @param mensagemDTO the mensagemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mensagemDTO, or with status {@code 400 (Bad Request)} if the mensagem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mensagems")
    public ResponseEntity<MensagemDTO> createMensagem(@Valid @RequestBody MensagemDTO mensagemDTO) throws URISyntaxException {
        log.debug("REST request to save Mensagem : {}", mensagemDTO);
        if (mensagemDTO.getId() != null) {
            throw new BadRequestAlertException("A new mensagem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MensagemDTO result = mensagemService.save(mensagemDTO);
        return ResponseEntity
            .created(new URI("/api/mensagems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mensagems/:id} : Updates an existing mensagem.
     *
     * @param id the id of the mensagemDTO to save.
     * @param mensagemDTO the mensagemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mensagemDTO,
     * or with status {@code 400 (Bad Request)} if the mensagemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mensagemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mensagems/{id}")
    public ResponseEntity<MensagemDTO> updateMensagem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MensagemDTO mensagemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Mensagem : {}, {}", id, mensagemDTO);
        if (mensagemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mensagemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mensagemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MensagemDTO result = mensagemService.update(mensagemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mensagemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mensagems/:id} : Partial updates given fields of an existing mensagem, field will ignore if it is null
     *
     * @param id the id of the mensagemDTO to save.
     * @param mensagemDTO the mensagemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mensagemDTO,
     * or with status {@code 400 (Bad Request)} if the mensagemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mensagemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mensagemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mensagems/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MensagemDTO> partialUpdateMensagem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MensagemDTO mensagemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Mensagem partially : {}, {}", id, mensagemDTO);
        if (mensagemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mensagemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mensagemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MensagemDTO> result = mensagemService.partialUpdate(mensagemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mensagemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /mensagems} : get all the mensagems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mensagems in body.
     */
    @GetMapping("/mensagems")
    public ResponseEntity<List<MensagemDTO>> getAllMensagems(
        MensagemCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Mensagems by criteria: {}", criteria);
        Page<MensagemDTO> page = mensagemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mensagems/count} : count all the mensagems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/mensagems/count")
    public ResponseEntity<Long> countMensagems(MensagemCriteria criteria) {
        log.debug("REST request to count Mensagems by criteria: {}", criteria);
        return ResponseEntity.ok().body(mensagemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /mensagems/:id} : get the "id" mensagem.
     *
     * @param id the id of the mensagemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mensagemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mensagems/{id}")
    public ResponseEntity<MensagemDTO> getMensagem(@PathVariable Long id) {
        log.debug("REST request to get Mensagem : {}", id);
        Optional<MensagemDTO> mensagemDTO = mensagemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mensagemDTO);
    }

    /**
     * {@code DELETE  /mensagems/:id} : delete the "id" mensagem.
     *
     * @param id the id of the mensagemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mensagems/{id}")
    public ResponseEntity<Void> deleteMensagem(@PathVariable Long id) {
        log.debug("REST request to delete Mensagem : {}", id);
        mensagemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
