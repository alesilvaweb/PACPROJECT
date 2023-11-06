package com.aapm.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.aapm.app.domain.DescontoConvenio} entity. This class is used
 * in {@link com.aapm.app.web.rest.DescontoConvenioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /desconto-convenios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DescontoConvenioCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter desconto;

    private StringFilter descricao;

    private LongFilter convenioId;

    private Boolean distinct;

    public DescontoConvenioCriteria() {}

    public DescontoConvenioCriteria(DescontoConvenioCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.desconto = other.desconto == null ? null : other.desconto.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
        this.convenioId = other.convenioId == null ? null : other.convenioId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DescontoConvenioCriteria copy() {
        return new DescontoConvenioCriteria(this);
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

    public StringFilter getDesconto() {
        return desconto;
    }

    public StringFilter desconto() {
        if (desconto == null) {
            desconto = new StringFilter();
        }
        return desconto;
    }

    public void setDesconto(StringFilter desconto) {
        this.desconto = desconto;
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
        final DescontoConvenioCriteria that = (DescontoConvenioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(desconto, that.desconto) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(convenioId, that.convenioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, desconto, descricao, convenioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DescontoConvenioCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (desconto != null ? "desconto=" + desconto + ", " : "") +
            (descricao != null ? "descricao=" + descricao + ", " : "") +
            (convenioId != null ? "convenioId=" + convenioId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
