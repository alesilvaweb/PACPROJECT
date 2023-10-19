package com.aapm.app.repository;

import com.aapm.app.domain.ImagensConvenio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ImagensConvenio entity.
 */
@Repository
public interface ImagensConvenioRepository extends JpaRepository<ImagensConvenio, Long>, JpaSpecificationExecutor<ImagensConvenio> {
    default Optional<ImagensConvenio> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ImagensConvenio> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ImagensConvenio> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct imagensConvenio from ImagensConvenio imagensConvenio left join fetch imagensConvenio.convenio",
        countQuery = "select count(distinct imagensConvenio) from ImagensConvenio imagensConvenio"
    )
    Page<ImagensConvenio> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct imagensConvenio from ImagensConvenio imagensConvenio left join fetch imagensConvenio.convenio")
    List<ImagensConvenio> findAllWithToOneRelationships();

    @Query(
        "select imagensConvenio from ImagensConvenio imagensConvenio left join fetch imagensConvenio.convenio where imagensConvenio.id =:id"
    )
    Optional<ImagensConvenio> findOneWithToOneRelationships(@Param("id") Long id);
}
