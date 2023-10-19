package com.aapm.app.service;

import com.aapm.app.domain.Dependente;
import com.aapm.app.repository.DependenteRepository;
import com.aapm.app.service.dto.DependenteDTO;
import com.aapm.app.service.mapper.DependenteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Dependente}.
 */
@Service
@Transactional
public class DependenteService {

    private final Logger log = LoggerFactory.getLogger(DependenteService.class);

    private final DependenteRepository dependenteRepository;

    private final DependenteMapper dependenteMapper;

    public DependenteService(DependenteRepository dependenteRepository, DependenteMapper dependenteMapper) {
        this.dependenteRepository = dependenteRepository;
        this.dependenteMapper = dependenteMapper;
    }

    /**
     * Save a dependente.
     *
     * @param dependenteDTO the entity to save.
     * @return the persisted entity.
     */
    public DependenteDTO save(DependenteDTO dependenteDTO) {
        log.debug("Request to save Dependente : {}", dependenteDTO);
        Dependente dependente = dependenteMapper.toEntity(dependenteDTO);
        dependente = dependenteRepository.save(dependente);
        return dependenteMapper.toDto(dependente);
    }

    /**
     * Update a dependente.
     *
     * @param dependenteDTO the entity to save.
     * @return the persisted entity.
     */
    public DependenteDTO update(DependenteDTO dependenteDTO) {
        log.debug("Request to update Dependente : {}", dependenteDTO);
        Dependente dependente = dependenteMapper.toEntity(dependenteDTO);
        dependente = dependenteRepository.save(dependente);
        return dependenteMapper.toDto(dependente);
    }

    /**
     * Partially update a dependente.
     *
     * @param dependenteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DependenteDTO> partialUpdate(DependenteDTO dependenteDTO) {
        log.debug("Request to partially update Dependente : {}", dependenteDTO);

        return dependenteRepository
            .findById(dependenteDTO.getId())
            .map(existingDependente -> {
                dependenteMapper.partialUpdate(existingDependente, dependenteDTO);

                return existingDependente;
            })
            .map(dependenteRepository::save)
            .map(dependenteMapper::toDto);
    }

    /**
     * Get all the dependentes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DependenteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Dependentes");
        return dependenteRepository.findAll(pageable).map(dependenteMapper::toDto);
    }

    /**
     * Get all the dependentes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DependenteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return dependenteRepository.findAllWithEagerRelationships(pageable).map(dependenteMapper::toDto);
    }

    /**
     * Get one dependente by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DependenteDTO> findOne(Long id) {
        log.debug("Request to get Dependente : {}", id);
        return dependenteRepository.findOneWithEagerRelationships(id).map(dependenteMapper::toDto);
    }

    /**
     * Delete the dependente by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Dependente : {}", id);
        dependenteRepository.deleteById(id);
    }
}
