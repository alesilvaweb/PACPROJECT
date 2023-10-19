package com.aapm.app.service;

import com.aapm.app.domain.*; // for static metamodels
import com.aapm.app.domain.Mensagem;
import com.aapm.app.repository.MensagemRepository;
import com.aapm.app.service.criteria.MensagemCriteria;
import com.aapm.app.service.dto.MensagemDTO;
import com.aapm.app.service.mapper.MensagemMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Mensagem} entities in the database.
 * The main input is a {@link MensagemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MensagemDTO} or a {@link Page} of {@link MensagemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MensagemQueryService extends QueryService<Mensagem> {

    private final Logger log = LoggerFactory.getLogger(MensagemQueryService.class);

    private final MensagemRepository mensagemRepository;

    private final MensagemMapper mensagemMapper;

    public MensagemQueryService(MensagemRepository mensagemRepository, MensagemMapper mensagemMapper) {
        this.mensagemRepository = mensagemRepository;
        this.mensagemMapper = mensagemMapper;
    }

    /**
     * Return a {@link List} of {@link MensagemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MensagemDTO> findByCriteria(MensagemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Mensagem> specification = createSpecification(criteria);
        return mensagemMapper.toDto(mensagemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MensagemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MensagemDTO> findByCriteria(MensagemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Mensagem> specification = createSpecification(criteria);
        return mensagemRepository.findAll(specification, page).map(mensagemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MensagemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Mensagem> specification = createSpecification(criteria);
        return mensagemRepository.count(specification);
    }

    /**
     * Function to convert {@link MensagemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Mensagem> createSpecification(MensagemCriteria criteria) {
        Specification<Mensagem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Mensagem_.id));
            }
            if (criteria.getTitulo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitulo(), Mensagem_.titulo));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), Mensagem_.descricao));
            }
            if (criteria.getLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLink(), Mensagem_.link));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Mensagem_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Mensagem_.endDate));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Mensagem_.status));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), Mensagem_.created));
            }
            if (criteria.getModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModified(), Mensagem_.modified));
            }
            if (criteria.getTipoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTipoId(), root -> root.join(Mensagem_.tipo, JoinType.LEFT).get(Tipo_.id))
                    );
            }
        }
        return specification;
    }
}
