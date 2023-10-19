package com.aapm.app.service.mapper;

import com.aapm.app.domain.Local;
import com.aapm.app.service.dto.LocalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Local} and its DTO {@link LocalDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocalMapper extends EntityMapper<LocalDTO, Local> {}
