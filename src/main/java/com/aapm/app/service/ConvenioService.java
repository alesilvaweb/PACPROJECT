package com.aapm.app.service;

import com.aapm.app.domain.Convenio;
import com.aapm.app.repository.ConvenioRepository;
import com.aapm.app.service.dto.ConvenioDTO;
import com.aapm.app.service.mapper.ConvenioMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Convenio}.
 */
@Service
@Transactional
public class ConvenioService {

    private final Logger log = LoggerFactory.getLogger(ConvenioService.class);

    private final ConvenioRepository convenioRepository;

    private final ConvenioMapper convenioMapper;

    public ConvenioService(ConvenioRepository convenioRepository, ConvenioMapper convenioMapper) {
        this.convenioRepository = convenioRepository;
        this.convenioMapper = convenioMapper;
    }

    /**
     * Save a convenio.
     *
     * @param convenioDTO the entity to save.
     * @return the persisted entity.
     */
    public ConvenioDTO save(ConvenioDTO convenioDTO) {
        log.debug("Request to save Convenio : {}", convenioDTO);
        Convenio convenio = convenioMapper.toEntity(convenioDTO);
        convenio = convenioRepository.save(convenio);
        return convenioMapper.toDto(convenio);
    }

    /**
     * Update a convenio.
     *
     * @param convenioDTO the entity to save.
     * @return the persisted entity.
     */
    public ConvenioDTO update(ConvenioDTO convenioDTO) {
        log.debug("Request to update Convenio : {}", convenioDTO);
        Convenio convenio = convenioMapper.toEntity(convenioDTO);
        convenio = convenioRepository.save(convenio);
        return convenioMapper.toDto(convenio);
    }

    /**
     * Partially update a convenio.
     *
     * @param convenioDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConvenioDTO> partialUpdate(ConvenioDTO convenioDTO) {
        log.debug("Request to partially update Convenio : {}", convenioDTO);

        return convenioRepository
            .findById(convenioDTO.getId())
            .map(existingConvenio -> {
                convenioMapper.partialUpdate(existingConvenio, convenioDTO);

                return existingConvenio;
            })
            .map(convenioRepository::save)
            .map(convenioMapper::toDto);
    }

    /**
     * Get all the convenios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ConvenioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Convenios");
        return convenioRepository.findAll(pageable).map(convenioMapper::toDto);
    }

    /**
     * Get all the convenios with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ConvenioDTO> findAllWithEagerRelationships(Pageable pageable) {
        return convenioRepository.findAllWithEagerRelationships(pageable).map(convenioMapper::toDto);
    }

    /**
     * Get one convenio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConvenioDTO> findOne(Long id) {
        log.debug("Request to get Convenio : {}", id);
        return convenioRepository.findOneWithEagerRelationships(id).map(convenioMapper::toDto);
    }

    /**
     * Delete the convenio by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Convenio : {}", id);
        convenioRepository.deleteById(id);
    }
}
