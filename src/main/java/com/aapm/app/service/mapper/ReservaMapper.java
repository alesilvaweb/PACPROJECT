package com.aapm.app.service.mapper;

import com.aapm.app.domain.Associado;
import com.aapm.app.domain.Departamento;
import com.aapm.app.domain.Local;
import com.aapm.app.domain.Reserva;
import com.aapm.app.service.dto.AssociadoDTO;
import com.aapm.app.service.dto.DepartamentoDTO;
import com.aapm.app.service.dto.LocalDTO;
import com.aapm.app.service.dto.ReservaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reserva} and its DTO {@link ReservaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReservaMapper extends EntityMapper<ReservaDTO, Reserva> {
    @Mapping(target = "local", source = "local", qualifiedByName = "localNome")
    @Mapping(target = "associado", source = "associado", qualifiedByName = "associadoNome")
    @Mapping(target = "departamento", source = "departamento", qualifiedByName = "departamentoNome")
    ReservaDTO toDto(Reserva s);

    @Named("localNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    LocalDTO toDtoLocalNome(Local local);

    @Named("associadoNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "telefone", source = "telefone")
    AssociadoDTO toDtoAssociadoNome(Associado associado);

    @Named("departamentoNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    DepartamentoDTO toDtoDepartamentoNome(Departamento departamento);
}
