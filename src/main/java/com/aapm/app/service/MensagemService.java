package com.aapm.app.service;

import com.aapm.app.domain.Mensagem;
import com.aapm.app.repository.MensagemRepository;
import com.aapm.app.service.dto.MensagemDTO;
import com.aapm.app.service.mapper.MensagemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Mensagem}.
 */
@Service
@Transactional
public class MensagemService {

    private final Logger log = LoggerFactory.getLogger(MensagemService.class);

    private final MensagemRepository mensagemRepository;

    private final MensagemMapper mensagemMapper;

    public MensagemService(MensagemRepository mensagemRepository, MensagemMapper mensagemMapper) {
        this.mensagemRepository = mensagemRepository;
        this.mensagemMapper = mensagemMapper;
    }

    /**
     * Save a mensagem.
     *
     * @param mensagemDTO the entity to save.
     * @return the persisted entity.
     */
    public MensagemDTO save(MensagemDTO mensagemDTO) {
        log.debug("Request to save Mensagem : {}", mensagemDTO);
        Mensagem mensagem = mensagemMapper.toEntity(mensagemDTO);
        mensagem = mensagemRepository.save(mensagem);
        return mensagemMapper.toDto(mensagem);
    }

    /**
     * Update a mensagem.
     *
     * @param mensagemDTO the entity to save.
     * @return the persisted entity.
     */
    public MensagemDTO update(MensagemDTO mensagemDTO) {
        log.debug("Request to update Mensagem : {}", mensagemDTO);
        Mensagem mensagem = mensagemMapper.toEntity(mensagemDTO);
        mensagem = mensagemRepository.save(mensagem);
        return mensagemMapper.toDto(mensagem);
    }

    /**
     * Partially update a mensagem.
     *
     * @param mensagemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MensagemDTO> partialUpdate(MensagemDTO mensagemDTO) {
        log.debug("Request to partially update Mensagem : {}", mensagemDTO);

        return mensagemRepository
            .findById(mensagemDTO.getId())
            .map(existingMensagem -> {
                mensagemMapper.partialUpdate(existingMensagem, mensagemDTO);

                return existingMensagem;
            })
            .map(mensagemRepository::save)
            .map(mensagemMapper::toDto);
    }

    /**
     * Get all the mensagems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MensagemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Mensagems");
        return mensagemRepository.findAll(pageable).map(mensagemMapper::toDto);
    }

    /**
     * Get all the mensagems with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MensagemDTO> findAllWithEagerRelationships(Pageable pageable) {
        return mensagemRepository.findAllWithEagerRelationships(pageable).map(mensagemMapper::toDto);
    }

    /**
     * Get one mensagem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MensagemDTO> findOne(Long id) {
        log.debug("Request to get Mensagem : {}", id);
        return mensagemRepository.findOneWithEagerRelationships(id).map(mensagemMapper::toDto);
    }

    /**
     * Delete the mensagem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Mensagem : {}", id);
        mensagemRepository.deleteById(id);
    }
}
