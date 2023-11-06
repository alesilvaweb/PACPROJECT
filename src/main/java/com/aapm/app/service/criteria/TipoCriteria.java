package com.aapm.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.aapm.app.domain.Tipo} entity. This class is used
 * in {@link com.aapm.app.web.rest.TipoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tipos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TipoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tipo;

    private StringFilter chave;

    private LongFilter mensagemId;

    private Boolean distinct;

    public TipoCriteria() {}

    public TipoCriteria(TipoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tipo = other.tipo == null ? null : other.tipo.copy();
        this.chave = other.chave == null ? null : other.chave.copy();
        this.mensagemId = other.mensagemId == null ? null : other.mensagemId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TipoCriteria copy() {
        return new TipoCriteria(this);
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

    public StringFilter getTipo() {
        return tipo;
    }

    public StringFilter tipo() {
        if (tipo == null) {
            tipo = new StringFilter();
        }
        return tipo;
    }

    public void setTipo(StringFilter tipo) {
        this.tipo = tipo;
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

    public LongFilter getMensagemId() {
        return mensagemId;
    }

    public LongFilter mensagemId() {
        if (mensagemId == null) {
            mensagemId = new LongFilter();
        }
        return mensagemId;
    }

    public void setMensagemId(LongFilter mensagemId) {
        this.mensagemId = mensagemId;
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
        final TipoCriteria that = (TipoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(chave, that.chave) &&
            Objects.equals(mensagemId, that.mensagemId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipo, chave, mensagemId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tipo != null ? "tipo=" + tipo + ", " : "") +
            (chave != null ? "chave=" + chave + ", " : "") +
            (mensagemId != null ? "mensagemId=" + mensagemId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
