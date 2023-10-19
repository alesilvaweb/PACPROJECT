package com.aapm.app.service;

import com.aapm.app.domain.*; // for static metamodels
import com.aapm.app.domain.Tipo;
import com.aapm.app.repository.TipoRepository;
import com.aapm.app.service.criteria.TipoCriteria;
import com.aapm.app.service.dto.TipoDTO;
import com.aapm.app.service.mapper.TipoMapper;
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
 * Service for executing complex queries for {@link Tipo} entities in the database.
 * The main input is a {@link TipoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TipoDTO} or a {@link Page} of {@link TipoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TipoQueryService extends QueryService<Tipo> {

    private final Logger log = LoggerFactory.getLogger(TipoQueryService.class);

    private final TipoRepository tipoRepository;

    private final TipoMapper tipoMapper;

    public TipoQueryService(TipoRepository tipoRepository, TipoMapper tipoMapper) {
        this.tipoRepository = tipoRepository;
        this.tipoMapper = tipoMapper;
    }

    /**
     * Return a {@link List} of {@link TipoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TipoDTO> findByCriteria(TipoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Tipo> specification = createSpecification(criteria);
        return tipoMapper.toDto(tipoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TipoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TipoDTO> findByCriteria(TipoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Tipo> specification = createSpecification(criteria);
        return tipoRepository.findAll(specification, page).map(tipoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TipoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Tipo> specification = createSpecification(criteria);
        return tipoRepository.count(specification);
    }

    /**
     * Function to convert {@link TipoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Tipo> createSpecification(TipoCriteria criteria) {
        Specification<Tipo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Tipo_.id));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTipo(), Tipo_.tipo));
            }
            if (criteria.getMensagemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMensagemId(), root -> root.join(Tipo_.mensagems, JoinType.LEFT).get(Mensagem_.id))
                    );
            }
        }
        return specification;
    }
}
