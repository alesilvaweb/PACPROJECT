package com.aapm.app.service;

import com.aapm.app.domain.*; // for static metamodels
import com.aapm.app.domain.DescontoConvenio;
import com.aapm.app.repository.DescontoConvenioRepository;
import com.aapm.app.service.criteria.DescontoConvenioCriteria;
import com.aapm.app.service.dto.DescontoConvenioDTO;
import com.aapm.app.service.mapper.DescontoConvenioMapper;
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
 * Service for executing complex queries for {@link DescontoConvenio} entities in the database.
 * The main input is a {@link DescontoConvenioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DescontoConvenioDTO} or a {@link Page} of {@link DescontoConvenioDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DescontoConvenioQueryService extends QueryService<DescontoConvenio> {

    private final Logger log = LoggerFactory.getLogger(DescontoConvenioQueryService.class);

    private final DescontoConvenioRepository descontoConvenioRepository;

    private final DescontoConvenioMapper descontoConvenioMapper;

    public DescontoConvenioQueryService(
        DescontoConvenioRepository descontoConvenioRepository,
        DescontoConvenioMapper descontoConvenioMapper
    ) {
        this.descontoConvenioRepository = descontoConvenioRepository;
        this.descontoConvenioMapper = descontoConvenioMapper;
    }

    /**
     * Return a {@link List} of {@link DescontoConvenioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DescontoConvenioDTO> findByCriteria(DescontoConvenioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DescontoConvenio> specification = createSpecification(criteria);
        return descontoConvenioMapper.toDto(descontoConvenioRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DescontoConvenioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DescontoConvenioDTO> findByCriteria(DescontoConvenioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DescontoConvenio> specification = createSpecification(criteria);
        return descontoConvenioRepository.findAll(specification, page).map(descontoConvenioMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DescontoConvenioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DescontoConvenio> specification = createSpecification(criteria);
        return descontoConvenioRepository.count(specification);
    }

    /**
     * Function to convert {@link DescontoConvenioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DescontoConvenio> createSpecification(DescontoConvenioCriteria criteria) {
        Specification<DescontoConvenio> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DescontoConvenio_.id));
            }
            if (criteria.getDesconto() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDesconto(), DescontoConvenio_.desconto));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), DescontoConvenio_.descricao));
            }
            if (criteria.getConvenioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConvenioId(),
                            root -> root.join(DescontoConvenio_.convenio, JoinType.LEFT).get(Convenio_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
