package com.aapm.app.service;

import com.aapm.app.domain.Associado;
import com.aapm.app.repository.AssociadoRepository;
import com.aapm.app.service.dto.AssociadoDTO;
import com.aapm.app.service.mapper.AssociadoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Associado}.
 */
@Service
@Transactional
public class AssociadoService {

    private final Logger log = LoggerFactory.getLogger(AssociadoService.class);

    private final AssociadoRepository associadoRepository;

    private final AssociadoMapper associadoMapper;

    public AssociadoService(AssociadoRepository associadoRepository, AssociadoMapper associadoMapper) {
        this.associadoRepository = associadoRepository;
        this.associadoMapper = associadoMapper;
    }

    /**
     * Save a associado.
     *
     * @param associadoDTO the entity to save.
     * @return the persisted entity.
     */
    public AssociadoDTO save(AssociadoDTO associadoDTO) {
        log.debug("Request to save Associado : {}", associadoDTO);
        Associado associado = associadoMapper.toEntity(associadoDTO);
        associado = associadoRepository.save(associado);
        return associadoMapper.toDto(associado);
    }

    /**
     * Update a associado.
     *
     * @param associadoDTO the entity to save.
     * @return the persisted entity.
     */
    public AssociadoDTO update(AssociadoDTO associadoDTO) {
        log.debug("Request to update Associado : {}", associadoDTO);
        Associado associado = associadoMapper.toEntity(associadoDTO);
        associado = associadoRepository.save(associado);
        return associadoMapper.toDto(associado);
    }

    /**
     * Partially update a associado.
     *
     * @param associadoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AssociadoDTO> partialUpdate(AssociadoDTO associadoDTO) {
        log.debug("Request to partially update Associado : {}", associadoDTO);

        return associadoRepository
            .findById(associadoDTO.getId())
            .map(existingAssociado -> {
                associadoMapper.partialUpdate(existingAssociado, associadoDTO);

                return existingAssociado;
            })
            .map(associadoRepository::save)
            .map(associadoMapper::toDto);
    }

    /**
     * Get all the associados.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AssociadoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Associados");
        return associadoRepository.findAll(pageable).map(associadoMapper::toDto);
    }

    /**
     * Get one associado by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AssociadoDTO> findOne(Long id) {
        log.debug("Request to get Associado : {}", id);
        return associadoRepository.findById(id).map(associadoMapper::toDto);
    }

    /**
     * Delete the associado by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Associado : {}", id);
        associadoRepository.deleteById(id);
    }
}
