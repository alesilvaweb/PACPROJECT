package com.aapm.app.service;

import com.aapm.app.domain.*; // for static metamodels
import com.aapm.app.domain.IconsRedesSociais;
import com.aapm.app.repository.IconsRedesSociaisRepository;
import com.aapm.app.service.criteria.IconsRedesSociaisCriteria;
import com.aapm.app.service.dto.IconsRedesSociaisDTO;
import com.aapm.app.service.mapper.IconsRedesSociaisMapper;
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
 * Service for executing complex queries for {@link IconsRedesSociais} entities in the database.
 * The main input is a {@link IconsRedesSociaisCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IconsRedesSociaisDTO} or a {@link Page} of {@link IconsRedesSociaisDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IconsRedesSociaisQueryService extends QueryService<IconsRedesSociais> {

    private final Logger log = LoggerFactory.getLogger(IconsRedesSociaisQueryService.class);

    private final IconsRedesSociaisRepository iconsRedesSociaisRepository;

    private final IconsRedesSociaisMapper iconsRedesSociaisMapper;

    public IconsRedesSociaisQueryService(
        IconsRedesSociaisRepository iconsRedesSociaisRepository,
        IconsRedesSociaisMapper iconsRedesSociaisMapper
    ) {
        this.iconsRedesSociaisRepository = iconsRedesSociaisRepository;
        this.iconsRedesSociaisMapper = iconsRedesSociaisMapper;
    }

    /**
     * Return a {@link List} of {@link IconsRedesSociaisDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IconsRedesSociaisDTO> findByCriteria(IconsRedesSociaisCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<IconsRedesSociais> specification = createSpecification(criteria);
        return iconsRedesSociaisMapper.toDto(iconsRedesSociaisRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link IconsRedesSociaisDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IconsRedesSociaisDTO> findByCriteria(IconsRedesSociaisCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IconsRedesSociais> specification = createSpecification(criteria);
        return iconsRedesSociaisRepository.findAll(specification, page).map(iconsRedesSociaisMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IconsRedesSociaisCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<IconsRedesSociais> specification = createSpecification(criteria);
        return iconsRedesSociaisRepository.count(specification);
    }

    /**
     * Function to convert {@link IconsRedesSociaisCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IconsRedesSociais> createSpecification(IconsRedesSociaisCriteria criteria) {
        Specification<IconsRedesSociais> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), IconsRedesSociais_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), IconsRedesSociais_.nome));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), IconsRedesSociais_.descricao));
            }
            if (criteria.getIcon() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIcon(), IconsRedesSociais_.icon));
            }
            if (criteria.getRedeSocialId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRedeSocialId(),
                            root -> root.join(IconsRedesSociais_.redeSocials, JoinType.LEFT).get(RedesSociaisConvenio_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
