package com.aapm.app.repository;

import com.aapm.app.domain.RedesSociaisConvenio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RedesSociaisConvenio entity.
 */
@Repository
public interface RedesSociaisConvenioRepository
    extends JpaRepository<RedesSociaisConvenio, Long>, JpaSpecificationExecutor<RedesSociaisConvenio> {
    default Optional<RedesSociaisConvenio> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<RedesSociaisConvenio> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<RedesSociaisConvenio> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct redesSociaisConvenio from RedesSociaisConvenio redesSociaisConvenio left join fetch redesSociaisConvenio.icon left join fetch redesSociaisConvenio.convenio",
        countQuery = "select count(distinct redesSociaisConvenio) from RedesSociaisConvenio redesSociaisConvenio"
    )
    Page<RedesSociaisConvenio> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct redesSociaisConvenio from RedesSociaisConvenio redesSociaisConvenio left join fetch redesSociaisConvenio.icon left join fetch redesSociaisConvenio.convenio"
    )
    List<RedesSociaisConvenio> findAllWithToOneRelationships();

    @Query(
        "select redesSociaisConvenio from RedesSociaisConvenio redesSociaisConvenio left join fetch redesSociaisConvenio.icon left join fetch redesSociaisConvenio.convenio where redesSociaisConvenio.id =:id"
    )
    Optional<RedesSociaisConvenio> findOneWithToOneRelationships(@Param("id") Long id);
}
