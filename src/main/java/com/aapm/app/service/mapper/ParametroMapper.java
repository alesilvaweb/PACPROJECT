package com.aapm.app.service.mapper;

import com.aapm.app.domain.Parametro;
import com.aapm.app.service.dto.ParametroDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Parametro} and its DTO {@link ParametroDTO}.
 */
@Mapper(componentModel = "spring")
public interface ParametroMapper extends EntityMapper<ParametroDTO, Parametro> {}
