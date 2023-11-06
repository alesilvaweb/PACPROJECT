package com.aapm.app.repository;

import com.aapm.app.domain.DescontoConvenio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DescontoConvenio entity.
 */
@Repository
public interface DescontoConvenioRepository extends JpaRepository<DescontoConvenio, Long>, JpaSpecificationExecutor<DescontoConvenio> {
    default Optional<DescontoConvenio> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DescontoConvenio> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DescontoConvenio> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct descontoConvenio from DescontoConvenio descontoConvenio left join fetch descontoConvenio.convenio",
        countQuery = "select count(distinct descontoConvenio) from DescontoConvenio descontoConvenio"
    )
    Page<DescontoConvenio> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct descontoConvenio from DescontoConvenio descontoConvenio left join fetch descontoConvenio.convenio")
    List<DescontoConvenio> findAllWithToOneRelationships();

    @Query(
        "select descontoConvenio from DescontoConvenio descontoConvenio left join fetch descontoConvenio.convenio where descontoConvenio.id =:id"
    )
    Optional<DescontoConvenio> findOneWithToOneRelationships(@Param("id") Long id);
}
