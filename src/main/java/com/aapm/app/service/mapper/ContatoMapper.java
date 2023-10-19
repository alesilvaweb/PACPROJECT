package com.aapm.app.service.mapper;

import com.aapm.app.domain.Associado;
import com.aapm.app.domain.Contato;
import com.aapm.app.service.dto.AssociadoDTO;
import com.aapm.app.service.dto.ContatoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contato} and its DTO {@link ContatoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContatoMapper extends EntityMapper<ContatoDTO, Contato> {
    @Mapping(target = "associado", source = "associado", qualifiedByName = "associadoNome")
    ContatoDTO toDto(Contato s);

    @Named("associadoNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    AssociadoDTO toDtoAssociadoNome(Associado associado);
}
