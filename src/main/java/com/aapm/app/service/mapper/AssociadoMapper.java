package com.aapm.app.service.mapper;

import com.aapm.app.domain.Associado;
import com.aapm.app.service.dto.AssociadoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Associado} and its DTO {@link AssociadoDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssociadoMapper extends EntityMapper<AssociadoDTO, Associado> {}
