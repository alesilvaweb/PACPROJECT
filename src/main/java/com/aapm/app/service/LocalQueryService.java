package com.aapm.app.service;

import com.aapm.app.domain.*; // for static metamodels
import com.aapm.app.domain.Local;
import com.aapm.app.repository.LocalRepository;
import com.aapm.app.service.criteria.LocalCriteria;
import com.aapm.app.service.dto.LocalDTO;
import com.aapm.app.service.mapper.LocalMapper;
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
 * Service for executing complex queries for {@link Local} entities in the database.
 * The main input is a {@link LocalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LocalDTO} or a {@link Page} of {@link LocalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LocalQueryService extends QueryService<Local> {

    private final Logger log = LoggerFactory.getLogger(LocalQueryService.class);

    private final LocalRepository localRepository;

    private final LocalMapper localMapper;

    public LocalQueryService(LocalRepository localRepository, LocalMapper localMapper) {
        this.localRepository = localRepository;
        this.localMapper = localMapper;
    }

    /**
     * Return a {@link List} of {@link LocalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LocalDTO> findByCriteria(LocalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Local> specification = createSpecification(criteria);
        return localMapper.toDto(localRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LocalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LocalDTO> findByCriteria(LocalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Local> specification = createSpecification(criteria);
        return localRepository.findAll(specification, page).map(localMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LocalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Local> specification = createSpecification(criteria);
        return localRepository.count(specification);
    }

    /**
     * Function to convert {@link LocalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Local> createSpecification(LocalCriteria criteria) {
        Specification<Local> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Local_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Local_.nome));
            }
            if (criteria.getCapacidade() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCapacidade(), Local_.capacidade));
            }
            if (criteria.getLocalizacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocalizacao(), Local_.localizacao));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Local_.status));
            }
            if (criteria.getValor() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValor(), Local_.valor));
            }
            if (criteria.getCor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCor(), Local_.cor));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), Local_.created));
            }
            if (criteria.getModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModified(), Local_.modified));
            }
            if (criteria.getReservaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getReservaId(), root -> root.join(Local_.reservas, JoinType.LEFT).get(Reserva_.id))
                    );
            }
        }
        return specification;
    }
}
