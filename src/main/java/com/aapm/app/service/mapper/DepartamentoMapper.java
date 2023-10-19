package com.aapm.app.service.mapper;

import com.aapm.app.domain.Departamento;
import com.aapm.app.service.dto.DepartamentoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Departamento} and its DTO {@link DepartamentoDTO}.
 */
@Mapper(componentModel = "spring")
public interface DepartamentoMapper extends EntityMapper<DepartamentoDTO, Departamento> {}
