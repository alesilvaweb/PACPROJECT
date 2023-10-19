package com.aapm.app.repository;

import com.aapm.app.domain.Convenio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Convenio entity.
 */
@Repository
public interface ConvenioRepository extends JpaRepository<Convenio, Long>, JpaSpecificationExecutor<Convenio> {
    default Optional<Convenio> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Convenio> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Convenio> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct convenio from Convenio convenio left join fetch convenio.categoria",
        countQuery = "select count(distinct convenio) from Convenio convenio"
    )
    Page<Convenio> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct convenio from Convenio convenio left join fetch convenio.categoria")
    List<Convenio> findAllWithToOneRelationships();

    @Query("select convenio from Convenio convenio left join fetch convenio.categoria where convenio.id =:id")
    Optional<Convenio> findOneWithToOneRelationships(@Param("id") Long id);
}
