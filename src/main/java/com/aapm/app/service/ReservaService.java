package com.aapm.app.service;

import com.aapm.app.domain.Reserva;
import com.aapm.app.repository.ReservaRepository;
import com.aapm.app.service.dto.ReservaDTO;
import com.aapm.app.service.mapper.ReservaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Reserva}.
 */
@Service
@Transactional
public class ReservaService {

    private final Logger log = LoggerFactory.getLogger(ReservaService.class);

    private final ReservaRepository reservaRepository;

    private final ReservaMapper reservaMapper;

    public ReservaService(ReservaRepository reservaRepository, ReservaMapper reservaMapper) {
        this.reservaRepository = reservaRepository;
        this.reservaMapper = reservaMapper;
    }

    /**
     * Save a reserva.
     *
     * @param reservaDTO the entity to save.
     * @return the persisted entity.
     */
    public ReservaDTO save(ReservaDTO reservaDTO) {
        log.debug("Request to save Reserva : {}", reservaDTO);
        Reserva reserva = reservaMapper.toEntity(reservaDTO);
        reserva = reservaRepository.save(reserva);
        return reservaMapper.toDto(reserva);
    }

    /**
     * Update a reserva.
     *
     * @param reservaDTO the entity to save.
     * @return the persisted entity.
     */
    public ReservaDTO update(ReservaDTO reservaDTO) {
        log.debug("Request to update Reserva : {}", reservaDTO);
        Reserva reserva = reservaMapper.toEntity(reservaDTO);
        reserva = reservaRepository.save(reserva);
        return reservaMapper.toDto(reserva);
    }

    /**
     * Partially update a reserva.
     *
     * @param reservaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReservaDTO> partialUpdate(ReservaDTO reservaDTO) {
        log.debug("Request to partially update Reserva : {}", reservaDTO);

        return reservaRepository
            .findById(reservaDTO.getId())
            .map(existingReserva -> {
                reservaMapper.partialUpdate(existingReserva, reservaDTO);

                return existingReserva;
            })
            .map(reservaRepository::save)
            .map(reservaMapper::toDto);
    }

    /**
     * Get all the reservas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ReservaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Reservas");
        return reservaRepository.findAll(pageable).map(reservaMapper::toDto);
    }

    /**
     * Get all the reservas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReservaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reservaRepository.findAllWithEagerRelationships(pageable).map(reservaMapper::toDto);
    }

    /**
     * Get one reserva by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReservaDTO> findOne(Long id) {
        log.debug("Request to get Reserva : {}", id);
        return reservaRepository.findOneWithEagerRelationships(id).map(reservaMapper::toDto);
    }

    /**
     * Delete the reserva by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Reserva : {}", id);
        reservaRepository.deleteById(id);
    }
}
