package com.aapm.app.web.rest;

import com.aapm.app.repository.ReservaRepository;
import com.aapm.app.service.ReservaQueryService;
import com.aapm.app.service.ReservaService;
import com.aapm.app.service.criteria.ReservaCriteria;
import com.aapm.app.service.dto.ReservaDTO;
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
 * REST controller for managing {@link com.aapm.app.domain.Reserva}.
 */
@RestController
@RequestMapping("/api")
public class ReservaResource {

    private final Logger log = LoggerFactory.getLogger(ReservaResource.class);

    private static final String ENTITY_NAME = "reserva";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReservaService reservaService;

    private final ReservaRepository reservaRepository;

    private final ReservaQueryService reservaQueryService;

    public ReservaResource(ReservaService reservaService, ReservaRepository reservaRepository, ReservaQueryService reservaQueryService) {
        this.reservaService = reservaService;
        this.reservaRepository = reservaRepository;
        this.reservaQueryService = reservaQueryService;
    }

    /**
     * {@code POST  /reservas} : Create a new reserva.
     *
     * @param reservaDTO the reservaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reservaDTO, or with status {@code 400 (Bad Request)} if the reserva has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reservas")
    public ResponseEntity<ReservaDTO> createReserva(@Valid @RequestBody ReservaDTO reservaDTO) throws URISyntaxException {
        log.debug("REST request to save Reserva : {}", reservaDTO);
        if (reservaDTO.getId() != null) {
            throw new BadRequestAlertException("A new reserva cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReservaDTO result = reservaService.save(reservaDTO);
        return ResponseEntity
            .created(new URI("/api/reservas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reservas/:id} : Updates an existing reserva.
     *
     * @param id the id of the reservaDTO to save.
     * @param reservaDTO the reservaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reservaDTO,
     * or with status {@code 400 (Bad Request)} if the reservaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reservaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reservas/{id}")
    public ResponseEntity<ReservaDTO> updateReserva(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReservaDTO reservaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Reserva : {}, {}", id, reservaDTO);
        if (reservaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reservaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reservaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReservaDTO result = reservaService.update(reservaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reservaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /reservas/:id} : Partial updates given fields of an existing reserva, field will ignore if it is null
     *
     * @param id the id of the reservaDTO to save.
     * @param reservaDTO the reservaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reservaDTO,
     * or with status {@code 400 (Bad Request)} if the reservaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reservaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reservaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reservas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReservaDTO> partialUpdateReserva(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReservaDTO reservaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Reserva partially : {}, {}", id, reservaDTO);
        if (reservaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reservaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reservaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReservaDTO> result = reservaService.partialUpdate(reservaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reservaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reservas} : get all the reservas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reservas in body.
     */
    @GetMapping("/reservas")
    public ResponseEntity<List<ReservaDTO>> getAllReservas(
        ReservaCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Reservas by criteria: {}", criteria);
        Page<ReservaDTO> page = reservaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reservas/count} : count all the reservas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/reservas/count")
    public ResponseEntity<Long> countReservas(ReservaCriteria criteria) {
        log.debug("REST request to count Reservas by criteria: {}", criteria);
        return ResponseEntity.ok().body(reservaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /reservas/:id} : get the "id" reserva.
     *
     * @param id the id of the reservaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reservaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reservas/{id}")
    public ResponseEntity<ReservaDTO> getReserva(@PathVariable Long id) {
        log.debug("REST request to get Reserva : {}", id);
        Optional<ReservaDTO> reservaDTO = reservaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reservaDTO);
    }

    /**
     * {@code DELETE  /reservas/:id} : delete the "id" reserva.
     *
     * @param id the id of the reservaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reservas/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable Long id) {
        log.debug("REST request to delete Reserva : {}", id);
        reservaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
