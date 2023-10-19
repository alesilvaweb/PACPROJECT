package com.aapm.app.service;

import com.aapm.app.domain.*; // for static metamodels
import com.aapm.app.domain.Associado;
import com.aapm.app.repository.AssociadoRepository;
import com.aapm.app.service.criteria.AssociadoCriteria;
import com.aapm.app.service.dto.AssociadoDTO;
import com.aapm.app.service.mapper.AssociadoMapper;
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
 * Service for executing complex queries for {@link Associado} entities in the database.
 * The main input is a {@link AssociadoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssociadoDTO} or a {@link Page} of {@link AssociadoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssociadoQueryService extends QueryService<Associado> {

    private final Logger log = LoggerFactory.getLogger(AssociadoQueryService.class);

    private final AssociadoRepository associadoRepository;

    private final AssociadoMapper associadoMapper;

    public AssociadoQueryService(AssociadoRepository associadoRepository, AssociadoMapper associadoMapper) {
        this.associadoRepository = associadoRepository;
        this.associadoMapper = associadoMapper;
    }

    /**
     * Return a {@link List} of {@link AssociadoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssociadoDTO> findByCriteria(AssociadoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Associado> specification = createSpecification(criteria);
        return associadoMapper.toDto(associadoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssociadoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssociadoDTO> findByCriteria(AssociadoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Associado> specification = createSpecification(criteria);
        return associadoRepository.findAll(specification, page).map(associadoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssociadoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Associado> specification = createSpecification(criteria);
        return associadoRepository.count(specification);
    }

    /**
     * Function to convert {@link AssociadoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Associado> createSpecification(AssociadoCriteria criteria) {
        Specification<Associado> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Associado_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Associado_.nome));
            }
            if (criteria.getMatricula() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMatricula(), Associado_.matricula));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Associado_.status));
            }
            if (criteria.getTelefone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelefone(), Associado_.telefone));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Associado_.email));
            }
            if (criteria.getDataNascimento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataNascimento(), Associado_.dataNascimento));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), Associado_.created));
            }
            if (criteria.getModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModified(), Associado_.modified));
            }
            if (criteria.getReservaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getReservaId(), root -> root.join(Associado_.reservas, JoinType.LEFT).get(Reserva_.id))
                    );
            }
            if (criteria.getContatosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getContatosId(), root -> root.join(Associado_.contatos, JoinType.LEFT).get(Contato_.id))
                    );
            }
            if (criteria.getDependentesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDependentesId(),
                            root -> root.join(Associado_.dependentes, JoinType.LEFT).get(Dependente_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
