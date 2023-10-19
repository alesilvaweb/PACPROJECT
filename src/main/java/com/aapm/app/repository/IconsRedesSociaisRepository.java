package com.aapm.app.repository;

import com.aapm.app.domain.IconsRedesSociais;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IconsRedesSociais entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IconsRedesSociaisRepository extends JpaRepository<IconsRedesSociais, Long>, JpaSpecificationExecutor<IconsRedesSociais> {}
