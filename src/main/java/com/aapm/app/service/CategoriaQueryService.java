package com.aapm.app.service;

import com.aapm.app.domain.*; // for static metamodels
import com.aapm.app.domain.Categoria;
import com.aapm.app.repository.CategoriaRepository;
import com.aapm.app.service.criteria.CategoriaCriteria;
import com.aapm.app.service.dto.CategoriaDTO;
import com.aapm.app.service.mapper.CategoriaMapper;
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
 * Service for executing complex queries for {@link Categoria} entities in the database.
 * The main input is a {@link CategoriaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CategoriaDTO} or a {@link Page} of {@link CategoriaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CategoriaQueryService extends QueryService<Categoria> {

    private final Logger log = LoggerFactory.getLogger(CategoriaQueryService.class);

    private final CategoriaRepository categoriaRepository;

    private final CategoriaMapper categoriaMapper;

    public CategoriaQueryService(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }

    /**
     * Return a {@link List} of {@link CategoriaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CategoriaDTO> findByCriteria(CategoriaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Categoria> specification = createSpecification(criteria);
        return categoriaMapper.toDto(categoriaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CategoriaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CategoriaDTO> findByCriteria(CategoriaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Categoria> specification = createSpecification(criteria);
        return categoriaRepository.findAll(specification, page).map(categoriaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CategoriaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Categoria> specification = createSpecification(criteria);
        return categoriaRepository.count(specification);
    }

    /**
     * Function to convert {@link CategoriaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Categoria> createSpecification(CategoriaCriteria criteria) {
        Specification<Categoria> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Categoria_.id));
            }
            if (criteria.getCategoria() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategoria(), Categoria_.categoria));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), Categoria_.descricao));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), Categoria_.created));
            }
            if (criteria.getModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModified(), Categoria_.modified));
            }
            if (criteria.getConvenioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConvenioId(),
                            root -> root.join(Categoria_.convenios, JoinType.LEFT).get(Convenio_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
