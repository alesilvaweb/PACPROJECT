package com.aapm.app.service;

import com.aapm.app.domain.IconsRedesSociais;
import com.aapm.app.repository.IconsRedesSociaisRepository;
import com.aapm.app.service.dto.IconsRedesSociaisDTO;
import com.aapm.app.service.mapper.IconsRedesSociaisMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IconsRedesSociais}.
 */
@Service
@Transactional
public class IconsRedesSociaisService {

    private final Logger log = LoggerFactory.getLogger(IconsRedesSociaisService.class);

    private final IconsRedesSociaisRepository iconsRedesSociaisRepository;

    private final IconsRedesSociaisMapper iconsRedesSociaisMapper;

    public IconsRedesSociaisService(
        IconsRedesSociaisRepository iconsRedesSociaisRepository,
        IconsRedesSociaisMapper iconsRedesSociaisMapper
    ) {
        this.iconsRedesSociaisRepository = iconsRedesSociaisRepository;
        this.iconsRedesSociaisMapper = iconsRedesSociaisMapper;
    }

    /**
     * Save a iconsRedesSociais.
     *
     * @param iconsRedesSociaisDTO the entity to save.
     * @return the persisted entity.
     */
    public IconsRedesSociaisDTO save(IconsRedesSociaisDTO iconsRedesSociaisDTO) {
        log.debug("Request to save IconsRedesSociais : {}", iconsRedesSociaisDTO);
        IconsRedesSociais iconsRedesSociais = iconsRedesSociaisMapper.toEntity(iconsRedesSociaisDTO);
        iconsRedesSociais = iconsRedesSociaisRepository.save(iconsRedesSociais);
        return iconsRedesSociaisMapper.toDto(iconsRedesSociais);
    }

    /**
     * Update a iconsRedesSociais.
     *
     * @param iconsRedesSociaisDTO the entity to save.
     * @return the persisted entity.
     */
    public IconsRedesSociaisDTO update(IconsRedesSociaisDTO iconsRedesSociaisDTO) {
        log.debug("Request to update IconsRedesSociais : {}", iconsRedesSociaisDTO);
        IconsRedesSociais iconsRedesSociais = iconsRedesSociaisMapper.toEntity(iconsRedesSociaisDTO);
        iconsRedesSociais = iconsRedesSociaisRepository.save(iconsRedesSociais);
        return iconsRedesSociaisMapper.toDto(iconsRedesSociais);
    }

    /**
     * Partially update a iconsRedesSociais.
     *
     * @param iconsRedesSociaisDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IconsRedesSociaisDTO> partialUpdate(IconsRedesSociaisDTO iconsRedesSociaisDTO) {
        log.debug("Request to partially update IconsRedesSociais : {}", iconsRedesSociaisDTO);

        return iconsRedesSociaisRepository
            .findById(iconsRedesSociaisDTO.getId())
            .map(existingIconsRedesSociais -> {
                iconsRedesSociaisMapper.partialUpdate(existingIconsRedesSociais, iconsRedesSociaisDTO);

                return existingIconsRedesSociais;
            })
            .map(iconsRedesSociaisRepository::save)
            .map(iconsRedesSociaisMapper::toDto);
    }

    /**
     * Get all the iconsRedesSociais.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<IconsRedesSociaisDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IconsRedesSociais");
        return iconsRedesSociaisRepository.findAll(pageable).map(iconsRedesSociaisMapper::toDto);
    }

    /**
     * Get one iconsRedesSociais by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IconsRedesSociaisDTO> findOne(Long id) {
        log.debug("Request to get IconsRedesSociais : {}", id);
        return iconsRedesSociaisRepository.findById(id).map(iconsRedesSociaisMapper::toDto);
    }

    /**
     * Delete the iconsRedesSociais by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete IconsRedesSociais : {}", id);
        iconsRedesSociaisRepository.deleteById(id);
    }
}
