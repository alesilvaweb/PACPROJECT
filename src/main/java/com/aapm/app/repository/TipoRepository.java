package com.aapm.app.repository;

import com.aapm.app.domain.Tipo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Tipo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoRepository extends JpaRepository<Tipo, Long>, JpaSpecificationExecutor<Tipo> {}
