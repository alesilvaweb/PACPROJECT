package com.aapm.app.repository;

import com.aapm.app.domain.Arquivo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Arquivo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArquivoRepository extends JpaRepository<Arquivo, Long>, JpaSpecificationExecutor<Arquivo> {}
