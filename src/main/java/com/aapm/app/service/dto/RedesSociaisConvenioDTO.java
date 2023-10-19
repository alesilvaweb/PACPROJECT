package com.aapm.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.aapm.app.domain.RedesSociaisConvenio} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RedesSociaisConvenioDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    private String descricao;

    @NotNull
    private String endereco;

    private Instant created;

    private Instant modified;

    private IconsRedesSociaisDTO icon;

    private ConvenioDTO convenio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
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

    public IconsRedesSociaisDTO getIcon() {
        return icon;
    }

    public void setIcon(IconsRedesSociaisDTO icon) {
        this.icon = icon;
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
        if (!(o instanceof RedesSociaisConvenioDTO)) {
            return false;
        }

        RedesSociaisConvenioDTO redesSociaisConvenioDTO = (RedesSociaisConvenioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, redesSociaisConvenioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RedesSociaisConvenioDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", endereco='" + getEndereco() + "'" +
            ", created='" + getCreated() + "'" +
            ", modified='" + getModified() + "'" +
            ", icon=" + getIcon() +
            ", convenio=" + getConvenio() +
            "}";
    }
}
