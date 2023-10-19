package com.aapm.app.service;

import com.aapm.app.domain.Contato;
import com.aapm.app.repository.ContatoRepository;
import com.aapm.app.service.dto.ContatoDTO;
import com.aapm.app.service.mapper.ContatoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Contato}.
 */
@Service
@Transactional
public class ContatoService {

    private final Logger log = LoggerFactory.getLogger(ContatoService.class);

    private final ContatoRepository contatoRepository;

    private final ContatoMapper contatoMapper;

    public ContatoService(ContatoRepository contatoRepository, ContatoMapper contatoMapper) {
        this.contatoRepository = contatoRepository;
        this.contatoMapper = contatoMapper;
    }

    /**
     * Save a contato.
     *
     * @param contatoDTO the entity to save.
     * @return the persisted entity.
     */
    public ContatoDTO save(ContatoDTO contatoDTO) {
        log.debug("Request to save Contato : {}", contatoDTO);
        Contato contato = contatoMapper.toEntity(contatoDTO);
        contato = contatoRepository.save(contato);
        return contatoMapper.toDto(contato);
    }

    /**
     * Update a contato.
     *
     * @param contatoDTO the entity to save.
     * @return the persisted entity.
     */
    public ContatoDTO update(ContatoDTO contatoDTO) {
        log.debug("Request to update Contato : {}", contatoDTO);
        Contato contato = contatoMapper.toEntity(contatoDTO);
        contato = contatoRepository.save(contato);
        return contatoMapper.toDto(contato);
    }

    /**
     * Partially update a contato.
     *
     * @param contatoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContatoDTO> partialUpdate(ContatoDTO contatoDTO) {
        log.debug("Request to partially update Contato : {}", contatoDTO);

        return contatoRepository
            .findById(contatoDTO.getId())
            .map(existingContato -> {
                contatoMapper.partialUpdate(existingContato, contatoDTO);

                return existingContato;
            })
            .map(contatoRepository::save)
            .map(contatoMapper::toDto);
    }

    /**
     * Get all the contatoes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContatoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contatoes");
        return contatoRepository.findAll(pageable).map(contatoMapper::toDto);
    }

    /**
     * Get all the contatoes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ContatoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return contatoRepository.findAllWithEagerRelationships(pageable).map(contatoMapper::toDto);
    }

    /**
     * Get one contato by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContatoDTO> findOne(Long id) {
        log.debug("Request to get Contato : {}", id);
        return contatoRepository.findOneWithEagerRelationships(id).map(contatoMapper::toDto);
    }

    /**
     * Delete the contato by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Contato : {}", id);
        contatoRepository.deleteById(id);
    }
}
