package com.aapm.app.repository;

import com.aapm.app.domain.Parametro;
import com.aapm.app.service.dto.ParametroDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Parametro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParametroRepository extends JpaRepository<Parametro, Long>, JpaSpecificationExecutor<Parametro> {
    Parametro findByChave(String s);
}
