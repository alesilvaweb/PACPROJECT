package com.aapm.app.repository;

import com.aapm.app.domain.Contato;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Contato entity.
 */
@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long>, JpaSpecificationExecutor<Contato> {
    default Optional<Contato> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Contato> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Contato> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct contato from Contato contato left join fetch contato.associado",
        countQuery = "select count(distinct contato) from Contato contato"
    )
    Page<Contato> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct contato from Contato contato left join fetch contato.associado")
    List<Contato> findAllWithToOneRelationships();

    @Query("select contato from Contato contato left join fetch contato.associado where contato.id =:id")
    Optional<Contato> findOneWithToOneRelationships(@Param("id") Long id);
}
