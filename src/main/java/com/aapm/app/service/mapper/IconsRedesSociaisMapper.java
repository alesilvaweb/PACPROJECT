package com.aapm.app.service.mapper;

import com.aapm.app.domain.IconsRedesSociais;
import com.aapm.app.service.dto.IconsRedesSociaisDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IconsRedesSociais} and its DTO {@link IconsRedesSociaisDTO}.
 */
@Mapper(componentModel = "spring")
public interface IconsRedesSociaisMapper extends EntityMapper<IconsRedesSociaisDTO, IconsRedesSociais> {}
