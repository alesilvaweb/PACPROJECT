package com.aapm.app.service.mapper;

import com.aapm.app.domain.Mensagem;
import com.aapm.app.domain.Tipo;
import com.aapm.app.service.dto.MensagemDTO;
import com.aapm.app.service.dto.TipoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Mensagem} and its DTO {@link MensagemDTO}.
 */
@Mapper(componentModel = "spring")
public interface MensagemMapper extends EntityMapper<MensagemDTO, Mensagem> {
    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "tipoTipo")
    MensagemDTO toDto(Mensagem s);

    @Named("tipoTipo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "tipo", source = "tipo")
    TipoDTO toDtoTipoTipo(Tipo tipo);
}
