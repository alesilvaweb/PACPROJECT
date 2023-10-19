package com.aapm.app.service.criteria;

import com.aapm.app.domain.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.aapm.app.domain.Local} entity. This class is used
 * in {@link com.aapm.app.web.rest.LocalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /locals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LocalCriteria implements Serializable, Criteria {

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

    private IntegerFilter capacidade;

    private StringFilter localizacao;

    private StatusFilter status;

    private FloatFilter valor;

    private StringFilter cor;

    private InstantFilter created;

    private InstantFilter modified;

    private LongFilter reservaId;

    private Boolean distinct;

    public LocalCriteria() {}

    public LocalCriteria(LocalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.capacidade = other.capacidade == null ? null : other.capacidade.copy();
        this.localizacao = other.localizacao == null ? null : other.localizacao.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.valor = other.valor == null ? null : other.valor.copy();
        this.cor = other.cor == null ? null : other.cor.copy();
        this.created = other.created == null ? null : other.created.copy();
        this.modified = other.modified == null ? null : other.modified.copy();
        this.reservaId = other.reservaId == null ? null : other.reservaId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LocalCriteria copy() {
        return new LocalCriteria(this);
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

    public IntegerFilter getCapacidade() {
        return capacidade;
    }

    public IntegerFilter capacidade() {
        if (capacidade == null) {
            capacidade = new IntegerFilter();
        }
        return capacidade;
    }

    public void setCapacidade(IntegerFilter capacidade) {
        this.capacidade = capacidade;
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

    public FloatFilter getValor() {
        return valor;
    }

    public FloatFilter valor() {
        if (valor == null) {
            valor = new FloatFilter();
        }
        return valor;
    }

    public void setValor(FloatFilter valor) {
        this.valor = valor;
    }

    public StringFilter getCor() {
        return cor;
    }

    public StringFilter cor() {
        if (cor == null) {
            cor = new StringFilter();
        }
        return cor;
    }

    public void setCor(StringFilter cor) {
        this.cor = cor;
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

    public LongFilter getReservaId() {
        return reservaId;
    }

    public LongFilter reservaId() {
        if (reservaId == null) {
            reservaId = new LongFilter();
        }
        return reservaId;
    }

    public void setReservaId(LongFilter reservaId) {
        this.reservaId = reservaId;
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
        final LocalCriteria that = (LocalCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(capacidade, that.capacidade) &&
            Objects.equals(localizacao, that.localizacao) &&
            Objects.equals(status, that.status) &&
            Objects.equals(valor, that.valor) &&
            Objects.equals(cor, that.cor) &&
            Objects.equals(created, that.created) &&
            Objects.equals(modified, that.modified) &&
            Objects.equals(reservaId, that.reservaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, capacidade, localizacao, status, valor, cor, created, modified, reservaId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocalCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (capacidade != null ? "capacidade=" + capacidade + ", " : "") +
            (localizacao != null ? "localizacao=" + localizacao + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (valor != null ? "valor=" + valor + ", " : "") +
            (cor != null ? "cor=" + cor + ", " : "") +
            (created != null ? "created=" + created + ", " : "") +
            (modified != null ? "modified=" + modified + ", " : "") +
            (reservaId != null ? "reservaId=" + reservaId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
