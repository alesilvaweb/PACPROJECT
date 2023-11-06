package com.aapm.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.aapm.app.domain.Tipo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TipoDTO implements Serializable {

    private Long id;

    @NotNull
    private String tipo;

    @NotNull
    private String chave;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoDTO)) {
            return false;
        }

        TipoDTO tipoDTO = (TipoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tipoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoDTO{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", chave='" + getChave() + "'" +
            "}";
    }
}
