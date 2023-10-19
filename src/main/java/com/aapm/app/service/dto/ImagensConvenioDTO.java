package com.aapm.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.aapm.app.domain.ImagensConvenio} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImagensConvenioDTO implements Serializable {

    private Long id;

    @NotNull
    private String titulo;

    private String descricao;

    @Lob
    private byte[] imagen;

    private String imagenContentType;
    private Instant created;

    private Instant modified;

    private ConvenioDTO convenio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getImagenContentType() {
        return imagenContentType;
    }

    public void setImagenContentType(String imagenContentType) {
        this.imagenContentType = imagenContentType;
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
        if (!(o instanceof ImagensConvenioDTO)) {
            return false;
        }

        ImagensConvenioDTO imagensConvenioDTO = (ImagensConvenioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, imagensConvenioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImagensConvenioDTO{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", imagen='" + getImagen() + "'" +
            ", created='" + getCreated() + "'" +
            ", modified='" + getModified() + "'" +
            ", convenio=" + getConvenio() +
            "}";
    }
}
