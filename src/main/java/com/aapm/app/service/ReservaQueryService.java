package com.aapm.app.service;

import com.aapm.app.domain.*; // for static metamodels
import com.aapm.app.domain.Reserva;
import com.aapm.app.repository.ReservaRepository;
import com.aapm.app.service.criteria.ReservaCriteria;
import com.aapm.app.service.dto.ReservaDTO;
import com.aapm.app.service.mapper.ReservaMapper;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.criteria.JoinType;
import javax.persistence.metamodel.SingularAttribute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import tech.jhipster.service.filter.LocalDateFilter;

/**
 * Service for executing complex queries for {@link Reserva} entities in the database.
 * The main input is a {@link ReservaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReservaDTO} or a {@link Page} of {@link ReservaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReservaQueryService extends QueryService<Reserva> {

    private final Logger log = LoggerFactory.getLogger(ReservaQueryService.class);

    private final ReservaRepository reservaRepository;

    private final ReservaMapper reservaMapper;

    public ReservaQueryService(ReservaRepository reservaRepository, ReservaMapper reservaMapper) {
        this.reservaRepository = reservaRepository;
        this.reservaMapper = reservaMapper;
    }

    /**
     * Return a {@link List} of {@link ReservaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReservaDTO> findByCriteria(ReservaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Reserva> specification = createSpecification(criteria);
        return reservaMapper.toDto(reservaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReservaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReservaDTO> findByCriteria(ReservaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Reserva> specification = createSpecification(criteria);
        return reservaRepository.findAll(specification, page).map(reservaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReservaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Reserva> specification = createSpecification(criteria);
        return reservaRepository.count(specification);
    }

    /**
     * Function to convert {@link ReservaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Reserva> createSpecification(ReservaCriteria criteria) {
        Specification<Reserva> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Reserva_.id));
            }
            if (criteria.getMotivoReserva() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMotivoReserva(), Reserva_.motivoReserva));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), Reserva_.descricao));
            }
            if (criteria.getNumPessoas() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumPessoas(), Reserva_.numPessoas));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Reserva_.status));
            }
            if (criteria.getData() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getData(), Reserva_.data));
            }
            if (criteria.getSomenteFuncionarios() != null) {
                specification = specification.and(buildSpecification(criteria.getSomenteFuncionarios(), Reserva_.somenteFuncionarios));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), Reserva_.created));
            }
            if (criteria.getModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModified(), Reserva_.modified));
            }
            if (criteria.getLocalId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLocalId(), root -> root.join(Reserva_.local, JoinType.LEFT).get(Local_.id))
                    );
            }
            if (criteria.getAssociadoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssociadoId(),
                            root -> root.join(Reserva_.associado, JoinType.LEFT).get(Associado_.id)
                        )
                    );
            }
            if (criteria.getDepartamentoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepartamentoId(),
                            root -> root.join(Reserva_.departamento, JoinType.LEFT).get(Departamento_.id)
                        )
                    );
            }
        }
        return specification;
    }


    private Specification<Reserva> buildRangeSpecification(LocalDateFilter data, SingularAttribute<Reserva, LocalDate> data1) {
        return null;
    }
}
