package com.aapm.app.service.mapper;

import com.aapm.app.domain.Categoria;
import com.aapm.app.domain.Convenio;
import com.aapm.app.service.dto.CategoriaDTO;
import com.aapm.app.service.dto.ConvenioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Convenio} and its DTO {@link ConvenioDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConvenioMapper extends EntityMapper<ConvenioDTO, Convenio> {
    @Mapping(target = "categoria", source = "categoria", qualifiedByName = "categoriaCategoria")
    ConvenioDTO toDto(Convenio s);

    @Named("categoriaCategoria")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "categoria", source = "categoria")
    CategoriaDTO toDtoCategoriaCategoria(Categoria categoria);
}
