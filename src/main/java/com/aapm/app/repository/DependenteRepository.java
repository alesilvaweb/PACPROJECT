package com.aapm.app.repository;

import com.aapm.app.domain.Dependente;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Dependente entity.
 */
@Repository
public interface DependenteRepository extends JpaRepository<Dependente, Long>, JpaSpecificationExecutor<Dependente> {
    default Optional<Dependente> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Dependente> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Dependente> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct dependente from Dependente dependente left join fetch dependente.associado",
        countQuery = "select count(distinct dependente) from Dependente dependente"
    )
    Page<Dependente> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct dependente from Dependente dependente left join fetch dependente.associado")
    List<Dependente> findAllWithToOneRelationships();

    @Query("select dependente from Dependente dependente left join fetch dependente.associado where dependente.id =:id")
    Optional<Dependente> findOneWithToOneRelationships(@Param("id") Long id);

    Optional<Object> findByNome(String nome);
}
