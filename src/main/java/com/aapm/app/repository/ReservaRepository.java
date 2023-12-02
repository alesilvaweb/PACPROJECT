package com.aapm.app.repository;

import com.aapm.app.domain.Local;
import com.aapm.app.domain.Reserva;
import com.aapm.app.service.dto.LocalDTO;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Reserva entity.
 */
@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long>, JpaSpecificationExecutor<Reserva> {
    default Optional<Reserva> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Reserva> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    List<Reserva> findReservaByDataAndLocal(@NotNull LocalDate data, Local local);

    default Page<Reserva> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct reserva from Reserva reserva left join fetch reserva.local left join fetch reserva.associado left join fetch reserva.departamento",
        countQuery = "select count(distinct reserva) from Reserva reserva"
    )
    Page<Reserva> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct reserva from Reserva reserva left join fetch reserva.local left join fetch reserva.associado left join fetch reserva.departamento"
    )
    List<Reserva> findAllWithToOneRelationships();

    @Query(
        "select reserva from Reserva reserva left join fetch reserva.local left join fetch reserva.associado left join fetch reserva.departamento where reserva.id =:id"
    )
    Optional<Reserva> findOneWithToOneRelationships(@Param("id") Long id);
}
