package com.aapm.app.service;

import com.aapm.app.domain.Tipo;
import com.aapm.app.repository.TipoRepository;
import com.aapm.app.service.dto.TipoDTO;
import com.aapm.app.service.mapper.TipoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tipo}.
 */
@Service
@Transactional
public class TipoService {

    private final Logger log = LoggerFactory.getLogger(TipoService.class);

    private final TipoRepository tipoRepository;

    private final TipoMapper tipoMapper;

    public TipoService(TipoRepository tipoRepository, TipoMapper tipoMapper) {
        this.tipoRepository = tipoRepository;
        this.tipoMapper = tipoMapper;
    }

    /**
     * Save a tipo.
     *
     * @param tipoDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoDTO save(TipoDTO tipoDTO) {
        log.debug("Request to save Tipo : {}", tipoDTO);
        Tipo tipo = tipoMapper.toEntity(tipoDTO);
        tipo = tipoRepository.save(tipo);
        return tipoMapper.toDto(tipo);
    }

    /**
     * Update a tipo.
     *
     * @param tipoDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoDTO update(TipoDTO tipoDTO) {
        log.debug("Request to update Tipo : {}", tipoDTO);
        Tipo tipo = tipoMapper.toEntity(tipoDTO);
        tipo = tipoRepository.save(tipo);
        return tipoMapper.toDto(tipo);
    }

    /**
     * Partially update a tipo.
     *
     * @param tipoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TipoDTO> partialUpdate(TipoDTO tipoDTO) {
        log.debug("Request to partially update Tipo : {}", tipoDTO);

        return tipoRepository
            .findById(tipoDTO.getId())
            .map(existingTipo -> {
                tipoMapper.partialUpdate(existingTipo, tipoDTO);

                return existingTipo;
            })
            .map(tipoRepository::save)
            .map(tipoMapper::toDto);
    }

    /**
     * Get all the tipos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TipoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tipos");
        return tipoRepository.findAll(pageable).map(tipoMapper::toDto);
    }

    /**
     * Get one tipo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TipoDTO> findOne(Long id) {
        log.debug("Request to get Tipo : {}", id);
        return tipoRepository.findById(id).map(tipoMapper::toDto);
    }

    /**
     * Delete the tipo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Tipo : {}", id);
        tipoRepository.deleteById(id);
    }
}
