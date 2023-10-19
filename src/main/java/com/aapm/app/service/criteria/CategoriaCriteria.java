package com.aapm.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.aapm.app.domain.Categoria} entity. This class is used
 * in {@link com.aapm.app.web.rest.CategoriaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /categorias?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoriaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter categoria;

    private StringFilter descricao;

    private InstantFilter created;

    private InstantFilter modified;

    private LongFilter convenioId;

    private Boolean distinct;

    public CategoriaCriteria() {}

    public CategoriaCriteria(CategoriaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.categoria = other.categoria == null ? null : other.categoria.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
        this.created = other.created == null ? null : other.created.copy();
        this.modified = other.modified == null ? null : other.modified.copy();
        this.convenioId = other.convenioId == null ? null : other.convenioId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CategoriaCriteria copy() {
        return new CategoriaCriteria(this);
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

    public StringFilter getCategoria() {
        return categoria;
    }

    public StringFilter categoria() {
        if (categoria == null) {
            categoria = new StringFilter();
        }
        return categoria;
    }

    public void setCategoria(StringFilter categoria) {
        this.categoria = categoria;
    }

    public StringFilter getDescricao() {
        return descricao;
    }

    public StringFilter descricao() {
        if (descricao == null) {
            descricao = new StringFilter();
        }
        return descricao;
    }

    public void setDescricao(StringFilter descricao) {
        this.descricao = descricao;
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

    public LongFilter getConvenioId() {
        return convenioId;
    }

    public LongFilter convenioId() {
        if (convenioId == null) {
            convenioId = new LongFilter();
        }
        return convenioId;
    }

    public void setConvenioId(LongFilter convenioId) {
        this.convenioId = convenioId;
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
        final CategoriaCriteria that = (CategoriaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(categoria, that.categoria) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(created, that.created) &&
            Objects.equals(modified, that.modified) &&
            Objects.equals(convenioId, that.convenioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoria, descricao, created, modified, convenioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoriaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (categoria != null ? "categoria=" + categoria + ", " : "") +
            (descricao != null ? "descricao=" + descricao + ", " : "") +
            (created != null ? "created=" + created + ", " : "") +
            (modified != null ? "modified=" + modified + ", " : "") +
            (convenioId != null ? "convenioId=" + convenioId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
