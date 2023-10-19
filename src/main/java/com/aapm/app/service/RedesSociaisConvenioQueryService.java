package com.aapm.app.service;

import com.aapm.app.domain.*; // for static metamodels
import com.aapm.app.domain.RedesSociaisConvenio;
import com.aapm.app.repository.RedesSociaisConvenioRepository;
import com.aapm.app.service.criteria.RedesSociaisConvenioCriteria;
import com.aapm.app.service.dto.RedesSociaisConvenioDTO;
import com.aapm.app.service.mapper.RedesSociaisConvenioMapper;
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
 * Service for executing complex queries for {@link RedesSociaisConvenio} entities in the database.
 * The main input is a {@link RedesSociaisConvenioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RedesSociaisConvenioDTO} or a {@link Page} of {@link RedesSociaisConvenioDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RedesSociaisConvenioQueryService extends QueryService<RedesSociaisConvenio> {

    private final Logger log = LoggerFactory.getLogger(RedesSociaisConvenioQueryService.class);

    private final RedesSociaisConvenioRepository redesSociaisConvenioRepository;

    private final RedesSociaisConvenioMapper redesSociaisConvenioMapper;

    public RedesSociaisConvenioQueryService(
        RedesSociaisConvenioRepository redesSociaisConvenioRepository,
        RedesSociaisConvenioMapper redesSociaisConvenioMapper
    ) {
        this.redesSociaisConvenioRepository = redesSociaisConvenioRepository;
        this.redesSociaisConvenioMapper = redesSociaisConvenioMapper;
    }

    /**
     * Return a {@link List} of {@link RedesSociaisConvenioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RedesSociaisConvenioDTO> findByCriteria(RedesSociaisConvenioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RedesSociaisConvenio> specification = createSpecification(criteria);
        return redesSociaisConvenioMapper.toDto(redesSociaisConvenioRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RedesSociaisConvenioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RedesSociaisConvenioDTO> findByCriteria(RedesSociaisConvenioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RedesSociaisConvenio> specification = createSpecification(criteria);
        return redesSociaisConvenioRepository.findAll(specification, page).map(redesSociaisConvenioMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RedesSociaisConvenioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RedesSociaisConvenio> specification = createSpecification(criteria);
        return redesSociaisConvenioRepository.count(specification);
    }

    /**
     * Function to convert {@link RedesSociaisConvenioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RedesSociaisConvenio> createSpecification(RedesSociaisConvenioCriteria criteria) {
        Specification<RedesSociaisConvenio> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RedesSociaisConvenio_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), RedesSociaisConvenio_.nome));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), RedesSociaisConvenio_.descricao));
            }
            if (criteria.getEndereco() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEndereco(), RedesSociaisConvenio_.endereco));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), RedesSociaisConvenio_.created));
            }
            if (criteria.getModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModified(), RedesSociaisConvenio_.modified));
            }
            if (criteria.getIconId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getIconId(),
                            root -> root.join(RedesSociaisConvenio_.icon, JoinType.LEFT).get(IconsRedesSociais_.id)
                        )
                    );
            }
            if (criteria.getConvenioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConvenioId(),
                            root -> root.join(RedesSociaisConvenio_.convenio, JoinType.LEFT).get(Convenio_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
