package com.aapm.app.service;

import com.aapm.app.domain.RedesSociaisConvenio;
import com.aapm.app.repository.RedesSociaisConvenioRepository;
import com.aapm.app.service.dto.RedesSociaisConvenioDTO;
import com.aapm.app.service.mapper.RedesSociaisConvenioMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RedesSociaisConvenio}.
 */
@Service
@Transactional
public class RedesSociaisConvenioService {

    private final Logger log = LoggerFactory.getLogger(RedesSociaisConvenioService.class);

    private final RedesSociaisConvenioRepository redesSociaisConvenioRepository;

    private final RedesSociaisConvenioMapper redesSociaisConvenioMapper;

    public RedesSociaisConvenioService(
        RedesSociaisConvenioRepository redesSociaisConvenioRepository,
        RedesSociaisConvenioMapper redesSociaisConvenioMapper
    ) {
        this.redesSociaisConvenioRepository = redesSociaisConvenioRepository;
        this.redesSociaisConvenioMapper = redesSociaisConvenioMapper;
    }

    /**
     * Save a redesSociaisConvenio.
     *
     * @param redesSociaisConvenioDTO the entity to save.
     * @return the persisted entity.
     */
    public RedesSociaisConvenioDTO save(RedesSociaisConvenioDTO redesSociaisConvenioDTO) {
        log.debug("Request to save RedesSociaisConvenio : {}", redesSociaisConvenioDTO);
        RedesSociaisConvenio redesSociaisConvenio = redesSociaisConvenioMapper.toEntity(redesSociaisConvenioDTO);
        redesSociaisConvenio = redesSociaisConvenioRepository.save(redesSociaisConvenio);
        return redesSociaisConvenioMapper.toDto(redesSociaisConvenio);
    }

    /**
     * Update a redesSociaisConvenio.
     *
     * @param redesSociaisConvenioDTO the entity to save.
     * @return the persisted entity.
     */
    public RedesSociaisConvenioDTO update(RedesSociaisConvenioDTO redesSociaisConvenioDTO) {
        log.debug("Request to update RedesSociaisConvenio : {}", redesSociaisConvenioDTO);
        RedesSociaisConvenio redesSociaisConvenio = redesSociaisConvenioMapper.toEntity(redesSociaisConvenioDTO);
        redesSociaisConvenio = redesSociaisConvenioRepository.save(redesSociaisConvenio);
        return redesSociaisConvenioMapper.toDto(redesSociaisConvenio);
    }

    /**
     * Partially update a redesSociaisConvenio.
     *
     * @param redesSociaisConvenioDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RedesSociaisConvenioDTO> partialUpdate(RedesSociaisConvenioDTO redesSociaisConvenioDTO) {
        log.debug("Request to partially update RedesSociaisConvenio : {}", redesSociaisConvenioDTO);

        return redesSociaisConvenioRepository
            .findById(redesSociaisConvenioDTO.getId())
            .map(existingRedesSociaisConvenio -> {
                redesSociaisConvenioMapper.partialUpdate(existingRedesSociaisConvenio, redesSociaisConvenioDTO);

                return existingRedesSociaisConvenio;
            })
            .map(redesSociaisConvenioRepository::save)
            .map(redesSociaisConvenioMapper::toDto);
    }

    /**
     * Get all the redesSociaisConvenios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RedesSociaisConvenioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RedesSociaisConvenios");
        return redesSociaisConvenioRepository.findAll(pageable).map(redesSociaisConvenioMapper::toDto);
    }

    /**
     * Get all the redesSociaisConvenios with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<RedesSociaisConvenioDTO> findAllWithEagerRelationships(Pageable pageable) {
        return redesSociaisConvenioRepository.findAllWithEagerRelationships(pageable).map(redesSociaisConvenioMapper::toDto);
    }

    /**
     * Get one redesSociaisConvenio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RedesSociaisConvenioDTO> findOne(Long id) {
        log.debug("Request to get RedesSociaisConvenio : {}", id);
        return redesSociaisConvenioRepository.findOneWithEagerRelationships(id).map(redesSociaisConvenioMapper::toDto);
    }

    /**
     * Delete the redesSociaisConvenio by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RedesSociaisConvenio : {}", id);
        redesSociaisConvenioRepository.deleteById(id);
    }
}
