package com.aapm.app.service;

import com.aapm.app.domain.*; // for static metamodels
import com.aapm.app.domain.Parametro;
import com.aapm.app.repository.ParametroRepository;
import com.aapm.app.service.criteria.ParametroCriteria;
import com.aapm.app.service.dto.ParametroDTO;
import com.aapm.app.service.mapper.ParametroMapper;
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
 * Service for executing complex queries for {@link Parametro} entities in the database.
 * The main input is a {@link ParametroCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ParametroDTO} or a {@link Page} of {@link ParametroDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ParametroQueryService extends QueryService<Parametro> {

    private final Logger log = LoggerFactory.getLogger(ParametroQueryService.class);

    private final ParametroRepository parametroRepository;

    private final ParametroMapper parametroMapper;

    public ParametroQueryService(ParametroRepository parametroRepository, ParametroMapper parametroMapper) {
        this.parametroRepository = parametroRepository;
        this.parametroMapper = parametroMapper;
    }

    /**
     * Return a {@link List} of {@link ParametroDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ParametroDTO> findByCriteria(ParametroCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Parametro> specification = createSpecification(criteria);
        return parametroMapper.toDto(parametroRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ParametroDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ParametroDTO> findByCriteria(ParametroCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Parametro> specification = createSpecification(criteria);
        return parametroRepository.findAll(specification, page).map(parametroMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ParametroCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Parametro> specification = createSpecification(criteria);
        return parametroRepository.count(specification);
    }

    /**
     * Function to convert {@link ParametroCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Parametro> createSpecification(ParametroCriteria criteria) {
        Specification<Parametro> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Parametro_.id));
            }
            if (criteria.getParametro() != null) {
                specification = specification.and(buildStringSpecification(criteria.getParametro(), Parametro_.parametro));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), Parametro_.descricao));
            }
            if (criteria.getChave() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChave(), Parametro_.chave));
            }
            if (criteria.getValor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValor(), Parametro_.valor));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Parametro_.status));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), Parametro_.created));
            }
            if (criteria.getModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModified(), Parametro_.modified));
            }
        }
        return specification;
    }
}
