package com.aapm.app.service;

import com.aapm.app.domain.*; // for static metamodels
import com.aapm.app.domain.Dependente;
import com.aapm.app.repository.DependenteRepository;
import com.aapm.app.service.criteria.DependenteCriteria;
import com.aapm.app.service.dto.DependenteDTO;
import com.aapm.app.service.mapper.DependenteMapper;
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
 * Service for executing complex queries for {@link Dependente} entities in the database.
 * The main input is a {@link DependenteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DependenteDTO} or a {@link Page} of {@link DependenteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DependenteQueryService extends QueryService<Dependente> {

    private final Logger log = LoggerFactory.getLogger(DependenteQueryService.class);

    private final DependenteRepository dependenteRepository;

    private final DependenteMapper dependenteMapper;

    public DependenteQueryService(DependenteRepository dependenteRepository, DependenteMapper dependenteMapper) {
        this.dependenteRepository = dependenteRepository;
        this.dependenteMapper = dependenteMapper;
    }

    /**
     * Return a {@link List} of {@link DependenteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DependenteDTO> findByCriteria(DependenteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Dependente> specification = createSpecification(criteria);
        return dependenteMapper.toDto(dependenteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DependenteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DependenteDTO> findByCriteria(DependenteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Dependente> specification = createSpecification(criteria);
        return dependenteRepository.findAll(specification, page).map(dependenteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DependenteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Dependente> specification = createSpecification(criteria);
        return dependenteRepository.count(specification);
    }

    /**
     * Function to convert {@link DependenteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Dependente> createSpecification(DependenteCriteria criteria) {
        Specification<Dependente> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Dependente_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Dependente_.nome));
            }
            if (criteria.getDataNascimento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataNascimento(), Dependente_.dataNascimento));
            }
            if (criteria.getParentesco() != null) {
                specification = specification.and(buildStringSpecification(criteria.getParentesco(), Dependente_.parentesco));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Dependente_.status));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), Dependente_.created));
            }
            if (criteria.getModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModified(), Dependente_.modified));
            }
            if (criteria.getAssociadoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssociadoId(),
                            root -> root.join(Dependente_.associado, JoinType.LEFT).get(Associado_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
