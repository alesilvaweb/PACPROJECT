package com.aapm.app.service.criteria;

import com.aapm.app.domain.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.aapm.app.domain.Convenio} entity. This class is used
 * in {@link com.aapm.app.web.rest.ConvenioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /convenios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConvenioCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Status
     */
    public static class StatusFilter extends Filter<Status> {

        public StatusFilter() {}

        public StatusFilter(StatusFilter filter) {
            super(filter);
        }

        @Override
        public StatusFilter copy() {
            return new StatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter titulo;

    private StringFilter endereco;

    private StringFilter telefone;

    private StringFilter email;

    private StringFilter localizacao;

    private StatusFilter status;

    private InstantFilter created;

    private InstantFilter modified;

    private LongFilter imagensId;

    private LongFilter redesSociaisId;

    private LongFilter categoriaId;

    private Boolean distinct;

    public ConvenioCriteria() {}

    public ConvenioCriteria(ConvenioCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.titulo = other.titulo == null ? null : other.titulo.copy();
        this.endereco = other.endereco == null ? null : other.endereco.copy();
        this.telefone = other.telefone == null ? null : other.telefone.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.localizacao = other.localizacao == null ? null : other.localizacao.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.created = other.created == null ? null : other.created.copy();
        this.modified = other.modified == null ? null : other.modified.copy();
        this.imagensId = other.imagensId == null ? null : other.imagensId.copy();
        this.redesSociaisId = other.redesSociaisId == null ? null : other.redesSociaisId.copy();
        this.categoriaId = other.categoriaId == null ? null : other.categoriaId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ConvenioCriteria copy() {
        return new ConvenioCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNome() {
        return nome;
    }

    public StringFilter nome() {
        if (nome == null) {
            nome = new StringFilter();
        }
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public StringFilter getTitulo() {
        return titulo;
    }

    public StringFilter titulo() {
        if (titulo == null) {
            titulo = new StringFilter();
        }
        return titulo;
    }

    public void setTitulo(StringFilter titulo) {
        this.titulo = titulo;
    }

    public StringFilter getEndereco() {
        return endereco;
    }

    public StringFilter endereco() {
        if (endereco == null) {
            endereco = new StringFilter();
        }
        return endereco;
    }

    public void setEndereco(StringFilter endereco) {
        this.endereco = endereco;
    }

    public StringFilter getTelefone() {
        return telefone;
    }

    public StringFilter telefone() {
        if (telefone == null) {
            telefone = new StringFilter();
        }
        return telefone;
    }

    public void setTelefone(StringFilter telefone) {
        this.telefone = telefone;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getLocalizacao() {
        return localizacao;
    }

    public StringFilter localizacao() {
        if (localizacao == null) {
            localizacao = new StringFilter();
        }
        return localizacao;
    }

    public void setLocalizacao(StringFilter localizacao) {
        this.localizacao = localizacao;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public StatusFilter status() {
        if (status == null) {
            status = new StatusFilter();
        }
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public InstantFilter getCreated() {
        return created;
    }

    public InstantFilter created() {
        if (created == null) {
            created = new InstantFilter();
        }
        return created;
    }

    public void setCreated(InstantFilter created) {
        this.created = created;
    }

    public InstantFilter getModified() {
        return modified;
    }

    public InstantFilter modified() {
        if (modified == null) {
            modified = new InstantFilter();
        }
        return modified;
    }

    public void setModified(InstantFilter modified) {
        this.modified = modified;
    }

    public LongFilter getImagensId() {
        return imagensId;
    }

    public LongFilter imagensId() {
        if (imagensId == null) {
            imagensId = new LongFilter();
        }
        return imagensId;
    }

    public void setImagensId(LongFilter imagensId) {
        this.imagensId = imagensId;
    }

    public LongFilter getRedesSociaisId() {
        return redesSociaisId;
    }

    public LongFilter redesSociaisId() {
        if (redesSociaisId == null) {
            redesSociaisId = new LongFilter();
        }
        return redesSociaisId;
    }

    public void setRedesSociaisId(LongFilter redesSociaisId) {
        this.redesSociaisId = redesSociaisId;
    }

    public LongFilter getCategoriaId() {
        return categoriaId;
    }

    public LongFilter categoriaId() {
        if (categoriaId == null) {
            categoriaId = new LongFilter();
        }
        return categoriaId;
    }

    public void setCategoriaId(LongFilter categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ConvenioCriteria that = (ConvenioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(titulo, that.titulo) &&
            Objects.equals(endereco, that.endereco) &&
            Objects.equals(telefone, that.telefone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(localizacao, that.localizacao) &&
            Objects.equals(status, that.status) &&
            Objects.equals(created, that.created) &&
            Objects.equals(modified, that.modified) &&
            Objects.equals(imagensId, that.imagensId) &&
            Objects.equals(redesSociaisId, that.redesSociaisId) &&
            Objects.equals(categoriaId, that.categoriaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nome,
            titulo,
            endereco,
            telefone,
            email,
            localizacao,
            status,
            created,
            modified,
            imagensId,
            redesSociaisId,
            categoriaId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConvenioCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (titulo != null ? "titulo=" + titulo + ", " : "") +
            (endereco != null ? "endereco=" + endereco + ", " : "") +
            (telefone != null ? "telefone=" + telefone + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (localizacao != null ? "localizacao=" + localizacao + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (created != null ? "created=" + created + ", " : "") +
            (modified != null ? "modified=" + modified + ", " : "") +
            (imagensId != null ? "imagensId=" + imagensId + ", " : "") +
            (redesSociaisId != null ? "redesSociaisId=" + redesSociaisId + ", " : "") +
            (categoriaId != null ? "categoriaId=" + categoriaId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
