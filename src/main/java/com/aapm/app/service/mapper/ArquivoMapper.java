package com.aapm.app.service.mapper;

import com.aapm.app.domain.Arquivo;
import com.aapm.app.service.dto.ArquivoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Arquivo} and its DTO {@link ArquivoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArquivoMapper extends EntityMapper<ArquivoDTO, Arquivo> {}
