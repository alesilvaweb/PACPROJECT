package com.aapm.app.repository;

import com.aapm.app.domain.Associado;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Associado entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssociadoRepository extends JpaRepository<Associado, Long>, JpaSpecificationExecutor<Associado> {}
