package com.aapm.app.service;

import com.aapm.app.domain.Local;
import com.aapm.app.repository.LocalRepository;
import com.aapm.app.service.dto.LocalDTO;
import com.aapm.app.service.mapper.LocalMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Local}.
 */
@Service
@Transactional
public class LocalService {

    private final Logger log = LoggerFactory.getLogger(LocalService.class);

    private final LocalRepository localRepository;

    private final LocalMapper localMapper;

    public LocalService(LocalRepository localRepository, LocalMapper localMapper) {
        this.localRepository = localRepository;
        this.localMapper = localMapper;
    }

    /**
     * Save a local.
     *
     * @param localDTO the entity to save.
     * @return the persisted entity.
     */
    public LocalDTO save(LocalDTO localDTO) {
        log.debug("Request to save Local : {}", localDTO);
        Local local = localMapper.toEntity(localDTO);
        local = localRepository.save(local);
        return localMapper.toDto(local);
    }

    /**
     * Update a local.
     *
     * @param localDTO the entity to save.
     * @return the persisted entity.
     */
    public LocalDTO update(LocalDTO localDTO) {
        log.debug("Request to update Local : {}", localDTO);
        Local local = localMapper.toEntity(localDTO);
        local = localRepository.save(local);
        return localMapper.toDto(local);
    }

    /**
     * Partially update a local.
     *
     * @param localDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LocalDTO> partialUpdate(LocalDTO localDTO) {
        log.debug("Request to partially update Local : {}", localDTO);

        return localRepository
            .findById(localDTO.getId())
            .map(existingLocal -> {
                localMapper.partialUpdate(existingLocal, localDTO);

                return existingLocal;
            })
            .map(localRepository::save)
            .map(localMapper::toDto);
    }

    /**
     * Get all the locals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LocalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Locals");
        return localRepository.findAll(pageable).map(localMapper::toDto);
    }

    /**
     * Get one local by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LocalDTO> findOne(Long id) {
        log.debug("Request to get Local : {}", id);
        return localRepository.findById(id).map(localMapper::toDto);
    }

    /**
     * Delete the local by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Local : {}", id);
        localRepository.deleteById(id);
    }
}
