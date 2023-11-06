package com.aapm.app.service;

import com.aapm.app.domain.DescontoConvenio;
import com.aapm.app.repository.DescontoConvenioRepository;
import com.aapm.app.service.dto.DescontoConvenioDTO;
import com.aapm.app.service.mapper.DescontoConvenioMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DescontoConvenio}.
 */
@Service
@Transactional
public class DescontoConvenioService {

    private final Logger log = LoggerFactory.getLogger(DescontoConvenioService.class);

    private final DescontoConvenioRepository descontoConvenioRepository;

    private final DescontoConvenioMapper descontoConvenioMapper;

    public DescontoConvenioService(DescontoConvenioRepository descontoConvenioRepository, DescontoConvenioMapper descontoConvenioMapper) {
        this.descontoConvenioRepository = descontoConvenioRepository;
        this.descontoConvenioMapper = descontoConvenioMapper;
    }

    /**
     * Save a descontoConvenio.
     *
     * @param descontoConvenioDTO the entity to save.
     * @return the persisted entity.
     */
    public DescontoConvenioDTO save(DescontoConvenioDTO descontoConvenioDTO) {
        log.debug("Request to save DescontoConvenio : {}", descontoConvenioDTO);
        DescontoConvenio descontoConvenio = descontoConvenioMapper.toEntity(descontoConvenioDTO);
        descontoConvenio = descontoConvenioRepository.save(descontoConvenio);
        return descontoConvenioMapper.toDto(descontoConvenio);
    }

    /**
     * Update a descontoConvenio.
     *
     * @param descontoConvenioDTO the entity to save.
     * @return the persisted entity.
     */
    public DescontoConvenioDTO update(DescontoConvenioDTO descontoConvenioDTO) {
        log.debug("Request to update DescontoConvenio : {}", descontoConvenioDTO);
        DescontoConvenio descontoConvenio = descontoConvenioMapper.toEntity(descontoConvenioDTO);
        descontoConvenio = descontoConvenioRepository.save(descontoConvenio);
        return descontoConvenioMapper.toDto(descontoConvenio);
    }

    /**
     * Partially update a descontoConvenio.
     *
     * @param descontoConvenioDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DescontoConvenioDTO> partialUpdate(DescontoConvenioDTO descontoConvenioDTO) {
        log.debug("Request to partially update DescontoConvenio : {}", descontoConvenioDTO);

        return descontoConvenioRepository
            .findById(descontoConvenioDTO.getId())
            .map(existingDescontoConvenio -> {
                descontoConvenioMapper.partialUpdate(existingDescontoConvenio, descontoConvenioDTO);

                return existingDescontoConvenio;
            })
            .map(descontoConvenioRepository::save)
            .map(descontoConvenioMapper::toDto);
    }

    /**
     * Get all the descontoConvenios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DescontoConvenioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DescontoConvenios");
        return descontoConvenioRepository.findAll(pageable).map(descontoConvenioMapper::toDto);
    }

    /**
     * Get all the descontoConvenios with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DescontoConvenioDTO> findAllWithEagerRelationships(Pageable pageable) {
        return descontoConvenioRepository.findAllWithEagerRelationships(pageable).map(descontoConvenioMapper::toDto);
    }

    /**
     * Get one descontoConvenio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DescontoConvenioDTO> findOne(Long id) {
        log.debug("Request to get DescontoConvenio : {}", id);
        return descontoConvenioRepository.findOneWithEagerRelationships(id).map(descontoConvenioMapper::toDto);
    }

    /**
     * Delete the descontoConvenio by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DescontoConvenio : {}", id);
        descontoConvenioRepository.deleteById(id);
    }
}
