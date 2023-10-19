package com.aapm.app.service;

import com.aapm.app.domain.*; // for static metamodels
import com.aapm.app.domain.ImagensConvenio;
import com.aapm.app.repository.ImagensConvenioRepository;
import com.aapm.app.service.criteria.ImagensConvenioCriteria;
import com.aapm.app.service.dto.ImagensConvenioDTO;
import com.aapm.app.service.mapper.ImagensConvenioMapper;
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
 * Service for executing complex queries for {@link ImagensConvenio} entities in the database.
 * The main input is a {@link ImagensConvenioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ImagensConvenioDTO} or a {@link Page} of {@link ImagensConvenioDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ImagensConvenioQueryService extends QueryService<ImagensConvenio> {

    private final Logger log = LoggerFactory.getLogger(ImagensConvenioQueryService.class);

    private final ImagensConvenioRepository imagensConvenioRepository;

    private final ImagensConvenioMapper imagensConvenioMapper;

    public ImagensConvenioQueryService(ImagensConvenioRepository imagensConvenioRepository, ImagensConvenioMapper imagensConvenioMapper) {
        this.imagensConvenioRepository = imagensConvenioRepository;
        this.imagensConvenioMapper = imagensConvenioMapper;
    }

    /**
     * Return a {@link List} of {@link ImagensConvenioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ImagensConvenioDTO> findByCriteria(ImagensConvenioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ImagensConvenio> specification = createSpecification(criteria);
        return imagensConvenioMapper.toDto(imagensConvenioRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ImagensConvenioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ImagensConvenioDTO> findByCriteria(ImagensConvenioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ImagensConvenio> specification = createSpecification(criteria);
        return imagensConvenioRepository.findAll(specification, page).map(imagensConvenioMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ImagensConvenioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ImagensConvenio> specification = createSpecification(criteria);
        return imagensConvenioRepository.count(specification);
    }

    /**
     * Function to convert {@link ImagensConvenioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ImagensConvenio> createSpecification(ImagensConvenioCriteria criteria) {
        Specification<ImagensConvenio> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ImagensConvenio_.id));
            }
            if (criteria.getTitulo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitulo(), ImagensConvenio_.titulo));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), ImagensConvenio_.descricao));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), ImagensConvenio_.created));
            }
            if (criteria.getModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModified(), ImagensConvenio_.modified));
            }
            if (criteria.getConvenioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConvenioId(),
                            root -> root.join(ImagensConvenio_.convenio, JoinType.LEFT).get(Convenio_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
