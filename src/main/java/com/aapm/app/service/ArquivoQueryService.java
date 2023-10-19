package com.aapm.app.service;

import com.aapm.app.domain.*; // for static metamodels
import com.aapm.app.domain.Arquivo;
import com.aapm.app.repository.ArquivoRepository;
import com.aapm.app.service.criteria.ArquivoCriteria;
import com.aapm.app.service.dto.ArquivoDTO;
import com.aapm.app.service.mapper.ArquivoMapper;
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
 * Service for executing complex queries for {@link Arquivo} entities in the database.
 * The main input is a {@link ArquivoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ArquivoDTO} or a {@link Page} of {@link ArquivoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ArquivoQueryService extends QueryService<Arquivo> {

    private final Logger log = LoggerFactory.getLogger(ArquivoQueryService.class);

    private final ArquivoRepository arquivoRepository;

    private final ArquivoMapper arquivoMapper;

    public ArquivoQueryService(ArquivoRepository arquivoRepository, ArquivoMapper arquivoMapper) {
        this.arquivoRepository = arquivoRepository;
        this.arquivoMapper = arquivoMapper;
    }

    /**
     * Return a {@link List} of {@link ArquivoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ArquivoDTO> findByCriteria(ArquivoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Arquivo> specification = createSpecification(criteria);
        return arquivoMapper.toDto(arquivoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ArquivoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ArquivoDTO> findByCriteria(ArquivoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Arquivo> specification = createSpecification(criteria);
        return arquivoRepository.findAll(specification, page).map(arquivoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ArquivoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Arquivo> specification = createSpecification(criteria);
        return arquivoRepository.count(specification);
    }

    /**
     * Function to convert {@link ArquivoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Arquivo> createSpecification(ArquivoCriteria criteria) {
        Specification<Arquivo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Arquivo_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Arquivo_.nome));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), Arquivo_.descricao));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Arquivo_.status));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), Arquivo_.created));
            }
            if (criteria.getModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModified(), Arquivo_.modified));
            }
        }
        return specification;
    }
}
