package com.aapm.app.service.dto;

import com.aapm.app.domain.enumeration.TipoContato;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.aapm.app.domain.Contato} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContatoDTO implements Serializable {

    private Long id;

    @NotNull
    private TipoContato tipo;

    @NotNull
    private String contato;

    private Instant created;

    private Instant modified;

    private AssociadoDTO associado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoContato getTipo() {
        return tipo;
    }

    public void setTipo(TipoContato tipo) {
        this.tipo = tipo;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getModified() {
        return modified;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    public AssociadoDTO getAssociado() {
        return associado;
    }

    public void setAssociado(AssociadoDTO associado) {
        this.associado = associado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContatoDTO)) {
            return false;
        }

        ContatoDTO contatoDTO = (ContatoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contatoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContatoDTO{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", contato='" + getContato() + "'" +
            ", created='" + getCreated() + "'" +
            ", modified='" + getModified() + "'" +
            ", associado=" + getAssociado() +
            "}";
    }
}
