package com.aapm.app.service.criteria;

import com.aapm.app.domain.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.aapm.app.domain.Parametro} entity. This class is used
 * in {@link com.aapm.app.web.rest.ParametroResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /parametros?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParametroCriteria implements Serializable, Criteria {

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

    private StringFilter parametro;

    private StringFilter descricao;

    private StringFilter chave;

    private StringFilter valor;

    private StatusFilter status;

    private InstantFilter created;

    private InstantFilter modified;

    private Boolean distinct;

    public ParametroCriteria() {}

    public ParametroCriteria(ParametroCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.parametro = other.parametro == null ? null : other.parametro.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
        this.chave = other.chave == null ? null : other.chave.copy();
        this.valor = other.valor == null ? null : other.valor.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.created = other.created == null ? null : other.created.copy();
        this.modified = other.modified == null ? null : other.modified.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ParametroCriteria copy() {
        return new ParametroCriteria(this);
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

    public StringFilter getParametro() {
        return parametro;
    }

    public StringFilter parametro() {
        if (parametro == null) {
            parametro = new StringFilter();
        }
        return parametro;
    }

    public void setParametro(StringFilter parametro) {
        this.parametro = parametro;
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

    public StringFilter getChave() {
        return chave;
    }

    public StringFilter chave() {
        if (chave == null) {
            chave = new StringFilter();
        }
        return chave;
    }

    public void setChave(StringFilter chave) {
        this.chave = chave;
    }

    public StringFilter getValor() {
        return valor;
    }

    public StringFilter valor() {
        if (valor == null) {
            valor = new StringFilter();
        }
        return valor;
    }

    public void setValor(StringFilter valor) {
        this.valor = valor;
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
        final ParametroCriteria that = (ParametroCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(parametro, that.parametro) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(chave, that.chave) &&
            Objects.equals(valor, that.valor) &&
            Objects.equals(status, that.status) &&
            Objects.equals(created, that.created) &&
            Objects.equals(modified, that.modified) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parametro, descricao, chave, valor, status, created, modified, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParametroCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (parametro != null ? "parametro=" + parametro + ", " : "") +
            (descricao != null ? "descricao=" + descricao + ", " : "") +
            (chave != null ? "chave=" + chave + ", " : "") +
            (valor != null ? "valor=" + valor + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (created != null ? "created=" + created + ", " : "") +
            (modified != null ? "modified=" + modified + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
