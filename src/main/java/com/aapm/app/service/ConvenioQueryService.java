package com.aapm.app.service;

import com.aapm.app.domain.*; // for static metamodels
import com.aapm.app.domain.Convenio;
import com.aapm.app.repository.ConvenioRepository;
import com.aapm.app.service.criteria.ConvenioCriteria;
import com.aapm.app.service.dto.ConvenioDTO;
import com.aapm.app.service.mapper.ConvenioMapper;
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
 * Service for executing complex queries for {@link Convenio} entities in the database.
 * The main input is a {@link ConvenioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ConvenioDTO} or a {@link Page} of {@link ConvenioDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConvenioQueryService extends QueryService<Convenio> {

    private final Logger log = LoggerFactory.getLogger(ConvenioQueryService.class);

    private final ConvenioRepository convenioRepository;

    private final ConvenioMapper convenioMapper;

    public ConvenioQueryService(ConvenioRepository convenioRepository, ConvenioMapper convenioMapper) {
        this.convenioRepository = convenioRepository;
        this.convenioMapper = convenioMapper;
    }

    /**
     * Return a {@link List} of {@link ConvenioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ConvenioDTO> findByCriteria(ConvenioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Convenio> specification = createSpecification(criteria);
        return convenioMapper.toDto(convenioRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ConvenioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ConvenioDTO> findByCriteria(ConvenioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Convenio> specification = createSpecification(criteria);
        return convenioRepository.findAll(specification, page).map(convenioMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConvenioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Convenio> specification = createSpecification(criteria);
        return convenioRepository.count(specification);
    }

    /**
     * Function to convert {@link ConvenioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Convenio> createSpecification(ConvenioCriteria criteria) {
        Specification<Convenio> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Convenio_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Convenio_.nome));
            }
            if (criteria.getTitulo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitulo(), Convenio_.titulo));
            }
            if (criteria.getEndereco() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEndereco(), Convenio_.endereco));
            }
            if (criteria.getTelefone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelefone(), Convenio_.telefone));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Convenio_.email));
            }
            if (criteria.getLocalizacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocalizacao(), Convenio_.localizacao));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Convenio_.status));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), Convenio_.created));
            }
            if (criteria.getModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModified(), Convenio_.modified));
            }
            if (criteria.getImagensId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getImagensId(),
                            root -> root.join(Convenio_.imagens, JoinType.LEFT).get(ImagensConvenio_.id)
                        )
                    );
            }
            if (criteria.getRedesSociaisId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRedesSociaisId(),
                            root -> root.join(Convenio_.redesSociais, JoinType.LEFT).get(RedesSociaisConvenio_.id)
                        )
                    );
            }
            if (criteria.getCategoriaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoriaId(),
                            root -> root.join(Convenio_.categoria, JoinType.LEFT).get(Categoria_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
