package com.aapm.app.repository;

import com.aapm.app.domain.Mensagem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Mensagem entity.
 */
@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Long>, JpaSpecificationExecutor<Mensagem> {
    default Optional<Mensagem> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Mensagem> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Mensagem> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct mensagem from Mensagem mensagem left join fetch mensagem.tipo",
        countQuery = "select count(distinct mensagem) from Mensagem mensagem"
    )
    Page<Mensagem> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct mensagem from Mensagem mensagem left join fetch mensagem.tipo")
    List<Mensagem> findAllWithToOneRelationships();

    @Query("select mensagem from Mensagem mensagem left join fetch mensagem.tipo where mensagem.id =:id")
    Optional<Mensagem> findOneWithToOneRelationships(@Param("id") Long id);
}
