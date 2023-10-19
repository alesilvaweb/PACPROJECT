package com.aapm.app.service.mapper;

import com.aapm.app.domain.Convenio;
import com.aapm.app.domain.IconsRedesSociais;
import com.aapm.app.domain.RedesSociaisConvenio;
import com.aapm.app.service.dto.ConvenioDTO;
import com.aapm.app.service.dto.IconsRedesSociaisDTO;
import com.aapm.app.service.dto.RedesSociaisConvenioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RedesSociaisConvenio} and its DTO {@link RedesSociaisConvenioDTO}.
 */
@Mapper(componentModel = "spring")
public interface RedesSociaisConvenioMapper extends EntityMapper<RedesSociaisConvenioDTO, RedesSociaisConvenio> {
    @Mapping(target = "icon", source = "icon", qualifiedByName = "iconsRedesSociaisNome")
    @Mapping(target = "convenio", source = "convenio", qualifiedByName = "convenioNome")
    RedesSociaisConvenioDTO toDto(RedesSociaisConvenio s);

    @Named("iconsRedesSociaisNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    IconsRedesSociaisDTO toDtoIconsRedesSociaisNome(IconsRedesSociais iconsRedesSociais);

    @Named("convenioNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    ConvenioDTO toDtoConvenioNome(Convenio convenio);
}
