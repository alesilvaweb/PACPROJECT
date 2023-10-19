package com.aapm.app.service.mapper;

import com.aapm.app.domain.Tipo;
import com.aapm.app.service.dto.TipoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tipo} and its DTO {@link TipoDTO}.
 */
@Mapper(componentModel = "spring")
public interface TipoMapper extends EntityMapper<TipoDTO, Tipo> {}
