package com.aapm.app.service.mapper;

import com.aapm.app.domain.Associado;
import com.aapm.app.domain.Dependente;
import com.aapm.app.service.dto.AssociadoDTO;
import com.aapm.app.service.dto.DependenteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dependente} and its DTO {@link DependenteDTO}.
 */
@Mapper(componentModel = "spring")
public interface DependenteMapper extends EntityMapper<DependenteDTO, Dependente> {
    @Mapping(target = "associado", source = "associado", qualifiedByName = "associadoNome")
    DependenteDTO toDto(Dependente s);

    @Named("associadoNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    AssociadoDTO toDtoAssociadoNome(Associado associado);
}
