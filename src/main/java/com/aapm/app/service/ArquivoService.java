package com.aapm.app.service;

import com.aapm.app.domain.Arquivo;
import com.aapm.app.repository.ArquivoRepository;
import com.aapm.app.service.dto.ArquivoDTO;
import com.aapm.app.service.mapper.ArquivoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Arquivo}.
 */
@Service
@Transactional
public class ArquivoService {

    private final Logger log = LoggerFactory.getLogger(ArquivoService.class);

    private final ArquivoRepository arquivoRepository;

    private final ArquivoMapper arquivoMapper;

    public ArquivoService(ArquivoRepository arquivoRepository, ArquivoMapper arquivoMapper) {
        this.arquivoRepository = arquivoRepository;
        this.arquivoMapper = arquivoMapper;
    }

    /**
     * Save a arquivo.
     *
     * @param arquivoDTO the entity to save.
     * @return the persisted entity.
     */
    public ArquivoDTO save(ArquivoDTO arquivoDTO) {
        log.debug("Request to save Arquivo : {}", arquivoDTO);
        Arquivo arquivo = arquivoMapper.toEntity(arquivoDTO);
        arquivo = arquivoRepository.save(arquivo);
        return arquivoMapper.toDto(arquivo);
    }

    /**
     * Update a arquivo.
     *
     * @param arquivoDTO the entity to save.
     * @return the persisted entity.
     */
    public ArquivoDTO update(ArquivoDTO arquivoDTO) {
        log.debug("Request to update Arquivo : {}", arquivoDTO);
        Arquivo arquivo = arquivoMapper.toEntity(arquivoDTO);
        arquivo = arquivoRepository.save(arquivo);
        return arquivoMapper.toDto(arquivo);
    }

    /**
     * Partially update a arquivo.
     *
     * @param arquivoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ArquivoDTO> partialUpdate(ArquivoDTO arquivoDTO) {
        log.debug("Request to partially update Arquivo : {}", arquivoDTO);

        return arquivoRepository
            .findById(arquivoDTO.getId())
            .map(existingArquivo -> {
                arquivoMapper.partialUpdate(existingArquivo, arquivoDTO);

                return existingArquivo;
            })
            .map(arquivoRepository::save)
            .map(arquivoMapper::toDto);
    }

    /**
     * Get all the arquivos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ArquivoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Arquivos");
        return arquivoRepository.findAll(pageable).map(arquivoMapper::toDto);
    }

    /**
     * Get one arquivo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ArquivoDTO> findOne(Long id) {
        log.debug("Request to get Arquivo : {}", id);
        return arquivoRepository.findById(id).map(arquivoMapper::toDto);
    }

    /**
     * Delete the arquivo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Arquivo : {}", id);
        arquivoRepository.deleteById(id);
    }
}
