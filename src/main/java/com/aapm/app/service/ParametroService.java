package com.aapm.app.service;

import com.aapm.app.domain.Parametro;
import com.aapm.app.repository.ParametroRepository;
import com.aapm.app.service.dto.ParametroDTO;
import com.aapm.app.service.mapper.ParametroMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Parametro}.
 */
@Service
@Transactional
public class ParametroService {

    private final Logger log = LoggerFactory.getLogger(ParametroService.class);

    private final ParametroRepository parametroRepository;

    private final ParametroMapper parametroMapper;

    public ParametroService(ParametroRepository parametroRepository, ParametroMapper parametroMapper) {
        this.parametroRepository = parametroRepository;
        this.parametroMapper = parametroMapper;
    }

    /**
     * Save a parametro.
     *
     * @param parametroDTO the entity to save.
     * @return the persisted entity.
     */
    public ParametroDTO save(ParametroDTO parametroDTO) {
        log.debug("Request to save Parametro : {}", parametroDTO);
        Parametro parametro = parametroMapper.toEntity(parametroDTO);
        parametro = parametroRepository.save(parametro);
        return parametroMapper.toDto(parametro);
    }

    /**
     * Update a parametro.
     *
     * @param parametroDTO the entity to save.
     * @return the persisted entity.
     */
    public ParametroDTO update(ParametroDTO parametroDTO) {
        log.debug("Request to update Parametro : {}", parametroDTO);
        Parametro parametro = parametroMapper.toEntity(parametroDTO);
        parametro = parametroRepository.save(parametro);
        return parametroMapper.toDto(parametro);
    }

    /**
     * Partially update a parametro.
     *
     * @param parametroDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ParametroDTO> partialUpdate(ParametroDTO parametroDTO) {
        log.debug("Request to partially update Parametro : {}", parametroDTO);

        return parametroRepository
            .findById(parametroDTO.getId())
            .map(existingParametro -> {
                parametroMapper.partialUpdate(existingParametro, parametroDTO);

                return existingParametro;
            })
            .map(parametroRepository::save)
            .map(parametroMapper::toDto);
    }

    /**
     * Get all the parametros.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ParametroDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Parametros");
        return parametroRepository.findAll(pageable).map(parametroMapper::toDto);
    }

    /**
     * Get one parametro by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParametroDTO> findOne(Long id) {
        log.debug("Request to get Parametro : {}", id);
        return parametroRepository.findById(id).map(parametroMapper::toDto);
    }

    /**
     * Delete the parametro by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Parametro : {}", id);
        parametroRepository.deleteById(id);
    }
}
