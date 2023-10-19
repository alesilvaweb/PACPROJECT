package com.aapm.app.service;

import com.aapm.app.domain.Departamento;
import com.aapm.app.repository.DepartamentoRepository;
import com.aapm.app.service.dto.DepartamentoDTO;
import com.aapm.app.service.mapper.DepartamentoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Departamento}.
 */
@Service
@Transactional
public class DepartamentoService {

    private final Logger log = LoggerFactory.getLogger(DepartamentoService.class);

    private final DepartamentoRepository departamentoRepository;

    private final DepartamentoMapper departamentoMapper;

    public DepartamentoService(DepartamentoRepository departamentoRepository, DepartamentoMapper departamentoMapper) {
        this.departamentoRepository = departamentoRepository;
        this.departamentoMapper = departamentoMapper;
    }

    /**
     * Save a departamento.
     *
     * @param departamentoDTO the entity to save.
     * @return the persisted entity.
     */
    public DepartamentoDTO save(DepartamentoDTO departamentoDTO) {
        log.debug("Request to save Departamento : {}", departamentoDTO);
        Departamento departamento = departamentoMapper.toEntity(departamentoDTO);
        departamento = departamentoRepository.save(departamento);
        return departamentoMapper.toDto(departamento);
    }

    /**
     * Update a departamento.
     *
     * @param departamentoDTO the entity to save.
     * @return the persisted entity.
     */
    public DepartamentoDTO update(DepartamentoDTO departamentoDTO) {
        log.debug("Request to update Departamento : {}", departamentoDTO);
        Departamento departamento = departamentoMapper.toEntity(departamentoDTO);
        departamento = departamentoRepository.save(departamento);
        return departamentoMapper.toDto(departamento);
    }

    /**
     * Partially update a departamento.
     *
     * @param departamentoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DepartamentoDTO> partialUpdate(DepartamentoDTO departamentoDTO) {
        log.debug("Request to partially update Departamento : {}", departamentoDTO);

        return departamentoRepository
            .findById(departamentoDTO.getId())
            .map(existingDepartamento -> {
                departamentoMapper.partialUpdate(existingDepartamento, departamentoDTO);

                return existingDepartamento;
            })
            .map(departamentoRepository::save)
            .map(departamentoMapper::toDto);
    }

    /**
     * Get all the departamentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DepartamentoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Departamentos");
        return departamentoRepository.findAll(pageable).map(departamentoMapper::toDto);
    }

    /**
     * Get one departamento by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DepartamentoDTO> findOne(Long id) {
        log.debug("Request to get Departamento : {}", id);
        return departamentoRepository.findById(id).map(departamentoMapper::toDto);
    }

    /**
     * Delete the departamento by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Departamento : {}", id);
        departamentoRepository.deleteById(id);
    }
}
