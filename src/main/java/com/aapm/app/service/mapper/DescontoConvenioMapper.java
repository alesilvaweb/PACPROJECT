package com.aapm.app.service.mapper;

import com.aapm.app.domain.Convenio;
import com.aapm.app.domain.DescontoConvenio;
import com.aapm.app.service.dto.ConvenioDTO;
import com.aapm.app.service.dto.DescontoConvenioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DescontoConvenio} and its DTO {@link DescontoConvenioDTO}.
 */
@Mapper(componentModel = "spring")
public interface DescontoConvenioMapper extends EntityMapper<DescontoConvenioDTO, DescontoConvenio> {
    @Mapping(target = "convenio", source = "convenio", qualifiedByName = "convenioNome")
    DescontoConvenioDTO toDto(DescontoConvenio s);

    @Named("convenioNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    ConvenioDTO toDtoConvenioNome(Convenio convenio);
}
