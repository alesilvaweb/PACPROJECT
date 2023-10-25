package com.aapm.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.aapm.app.domain.IconsRedesSociais} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IconsRedesSociaisDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    private String descricao;

    private String icon;

    @Lob
    private byte[] imagem;

    private String imagemContentType;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public String getImagemContentType() {
        return imagemContentType;
    }

    public void setImagemContentType(String imagemContentType) {
        this.imagemContentType = imagemContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IconsRedesSociaisDTO)) {
            return false;
        }

        IconsRedesSociaisDTO iconsRedesSociaisDTO = (IconsRedesSociaisDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, iconsRedesSociaisDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IconsRedesSociaisDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", icon='" + getIcon() + "'" +
            ", imagem='" + getImagem() + "'" +
            "}";
    }
}
