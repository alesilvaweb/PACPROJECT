package com.aapm.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.aapm.app.domain.DescontoConvenio} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DescontoConvenioDTO implements Serializable {

    private Long id;

    private String desconto;

    private String descricao;

    private ConvenioDTO convenio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesconto() {
        return desconto;
    }

    public void setDesconto(String desconto) {
        this.desconto = desconto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ConvenioDTO getConvenio() {
        return convenio;
    }

    public void setConvenio(ConvenioDTO convenio) {
        this.convenio = convenio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DescontoConvenioDTO)) {
            return false;
        }

        DescontoConvenioDTO descontoConvenioDTO = (DescontoConvenioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, descontoConvenioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DescontoConvenioDTO{" +
            "id=" + getId() +
            ", desconto='" + getDesconto() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", convenio=" + getConvenio() +
            "}";
    }
}
