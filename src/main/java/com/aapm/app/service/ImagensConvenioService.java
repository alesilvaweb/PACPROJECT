package com.aapm.app.service;

import com.aapm.app.domain.ImagensConvenio;
import com.aapm.app.repository.ImagensConvenioRepository;
import com.aapm.app.service.dto.ImagensConvenioDTO;
import com.aapm.app.service.mapper.ImagensConvenioMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ImagensConvenio}.
 */
@Service
@Transactional
public class ImagensConvenioService {

    private final Logger log = LoggerFactory.getLogger(ImagensConvenioService.class);

    private final ImagensConvenioRepository imagensConvenioRepository;

    private final ImagensConvenioMapper imagensConvenioMapper;

    public ImagensConvenioService(ImagensConvenioRepository imagensConvenioRepository, ImagensConvenioMapper imagensConvenioMapper) {
        this.imagensConvenioRepository = imagensConvenioRepository;
        this.imagensConvenioMapper = imagensConvenioMapper;
    }

    /**
     * Save a imagensConvenio.
     *
     * @param imagensConvenioDTO the entity to save.
     * @return the persisted entity.
     */
    public ImagensConvenioDTO save(ImagensConvenioDTO imagensConvenioDTO) {
        log.debug("Request to save ImagensConvenio : {}", imagensConvenioDTO);
        ImagensConvenio imagensConvenio = imagensConvenioMapper.toEntity(imagensConvenioDTO);
        imagensConvenio = imagensConvenioRepository.save(imagensConvenio);
        return imagensConvenioMapper.toDto(imagensConvenio);
    }

    /**
     * Update a imagensConvenio.
     *
     * @param imagensConvenioDTO the entity to save.
     * @return the persisted entity.
     */
    public ImagensConvenioDTO update(ImagensConvenioDTO imagensConvenioDTO) {
        log.debug("Request to update ImagensConvenio : {}", imagensConvenioDTO);
        ImagensConvenio imagensConvenio = imagensConvenioMapper.toEntity(imagensConvenioDTO);
        imagensConvenio = imagensConvenioRepository.save(imagensConvenio);
        return imagensConvenioMapper.toDto(imagensConvenio);
    }

    /**
     * Partially update a imagensConvenio.
     *
     * @param imagensConvenioDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ImagensConvenioDTO> partialUpdate(ImagensConvenioDTO imagensConvenioDTO) {
        log.debug("Request to partially update ImagensConvenio : {}", imagensConvenioDTO);

        return imagensConvenioRepository
            .findById(imagensConvenioDTO.getId())
            .map(existingImagensConvenio -> {
                imagensConvenioMapper.partialUpdate(existingImagensConvenio, imagensConvenioDTO);

                return existingImagensConvenio;
            })
            .map(imagensConvenioRepository::save)
            .map(imagensConvenioMapper::toDto);
    }

    /**
     * Get all the imagensConvenios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ImagensConvenioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ImagensConvenios");
        return imagensConvenioRepository.findAll(pageable).map(imagensConvenioMapper::toDto);
    }

    /**
     * Get all the imagensConvenios with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ImagensConvenioDTO> findAllWithEagerRelationships(Pageable pageable) {
        return imagensConvenioRepository.findAllWithEagerRelationships(pageable).map(imagensConvenioMapper::toDto);
    }

    /**
     * Get one imagensConvenio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ImagensConvenioDTO> findOne(Long id) {
        log.debug("Request to get ImagensConvenio : {}", id);
        return imagensConvenioRepository.findOneWithEagerRelationships(id).map(imagensConvenioMapper::toDto);
    }

    /**
     * Delete the imagensConvenio by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ImagensConvenio : {}", id);
        imagensConvenioRepository.deleteById(id);
    }
}
