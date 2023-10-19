package com.aapm.app.service.mapper;

import com.aapm.app.domain.Convenio;
import com.aapm.app.domain.ImagensConvenio;
import com.aapm.app.service.dto.ConvenioDTO;
import com.aapm.app.service.dto.ImagensConvenioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ImagensConvenio} and its DTO {@link ImagensConvenioDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImagensConvenioMapper extends EntityMapper<ImagensConvenioDTO, ImagensConvenio> {
    @Mapping(target = "convenio", source = "convenio", qualifiedByName = "convenioNome")
    ImagensConvenioDTO toDto(ImagensConvenio s);

    @Named("convenioNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    ConvenioDTO toDtoConvenioNome(Convenio convenio);
}
