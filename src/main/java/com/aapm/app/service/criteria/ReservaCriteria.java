package com.aapm.app.service.criteria;

import com.aapm.app.domain.enumeration.StatusReserva;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.aapm.app.domain.Reserva} entity. This class is used
 * in {@link com.aapm.app.web.rest.ReservaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reservas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReservaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatusReserva
     */
    public static class StatusReservaFilter extends Filter<StatusReserva> {

        public StatusReservaFilter() {}

        public StatusReservaFilter(StatusReservaFilter filter) {
            super(filter);
        }

        @Override
        public StatusReservaFilter copy() {
            return new StatusReservaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter motivoReserva;

    private StringFilter descricao;

    private IntegerFilter numPessoas;

    private StatusReservaFilter status;

    private LocalDateFilter data;

    private BooleanFilter somenteFuncionarios;

    private InstantFilter created;

    private InstantFilter modified;

    private LongFilter localId;

    private LongFilter associadoId;

    private LongFilter departamentoId;

    private Boolean distinct;

    public ReservaCriteria() {}

    public ReservaCriteria(ReservaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.motivoReserva = other.motivoReserva == null ? null : other.motivoReserva.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
        this.numPessoas = other.numPessoas == null ? null : other.numPessoas.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.data = other.data == null ? null : other.data.copy();
        this.somenteFuncionarios = other.somenteFuncionarios == null ? null : other.somenteFuncionarios.copy();
        this.created = other.created == null ? null : other.created.copy();
        this.modified = other.modified == null ? null : other.modified.copy();
        this.localId = other.localId == null ? null : other.localId.copy();
        this.associadoId = other.associadoId == null ? null : other.associadoId.copy();
        this.departamentoId = other.departamentoId == null ? null : other.departamentoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ReservaCriteria copy() {
        return new ReservaCriteria(this);
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

    public StringFilter getMotivoReserva() {
        return motivoReserva;
    }

    public StringFilter motivoReserva() {
        if (motivoReserva == null) {
            motivoReserva = new StringFilter();
        }
        return motivoReserva;
    }

    public void setMotivoReserva(StringFilter motivoReserva) {
        this.motivoReserva = motivoReserva;
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

    public IntegerFilter getNumPessoas() {
        return numPessoas;
    }

    public IntegerFilter numPessoas() {
        if (numPessoas == null) {
            numPessoas = new IntegerFilter();
        }
        return numPessoas;
    }

    public void setNumPessoas(IntegerFilter numPessoas) {
        this.numPessoas = numPessoas;
    }

    public StatusReservaFilter getStatus() {
        return status;
    }

    public StatusReservaFilter status() {
        if (status == null) {
            status = new StatusReservaFilter();
        }
        return status;
    }

    public void setStatus(StatusReservaFilter status) {
        this.status = status;
    }

    public LocalDateFilter getData() {
        return data;
    }

    public LocalDateFilter data() {
        if (data == null) {
            data = new LocalDateFilter();
        }
        return data;
    }

    public void setData(LocalDateFilter data) {
        this.data = data;
    }

    public BooleanFilter getSomenteFuncionarios() {
        return somenteFuncionarios;
    }

    public BooleanFilter somenteFuncionarios() {
        if (somenteFuncionarios == null) {
            somenteFuncionarios = new BooleanFilter();
        }
        return somenteFuncionarios;
    }

    public void setSomenteFuncionarios(BooleanFilter somenteFuncionarios) {
        this.somenteFuncionarios = somenteFuncionarios;
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

    public LongFilter getLocalId() {
        return localId;
    }

    public LongFilter localId() {
        if (localId == null) {
            localId = new LongFilter();
        }
        return localId;
    }

    public void setLocalId(LongFilter localId) {
        this.localId = localId;
    }

    public LongFilter getAssociadoId() {
        return associadoId;
    }

    public LongFilter associadoId() {
        if (associadoId == null) {
            associadoId = new LongFilter();
        }
        return associadoId;
    }

    public void setAssociadoId(LongFilter associadoId) {
        this.associadoId = associadoId;
    }

    public LongFilter getDepartamentoId() {
        return departamentoId;
    }

    public LongFilter departamentoId() {
        if (departamentoId == null) {
            departamentoId = new LongFilter();
        }
        return departamentoId;
    }

    public void setDepartamentoId(LongFilter departamentoId) {
        this.departamentoId = departamentoId;
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
        final ReservaCriteria that = (ReservaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(motivoReserva, that.motivoReserva) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(numPessoas, that.numPessoas) &&
            Objects.equals(status, that.status) &&
            Objects.equals(data, that.data) &&
            Objects.equals(somenteFuncionarios, that.somenteFuncionarios) &&
            Objects.equals(created, that.created) &&
            Objects.equals(modified, that.modified) &&
            Objects.equals(localId, that.localId) &&
            Objects.equals(associadoId, that.associadoId) &&
            Objects.equals(departamentoId, that.departamentoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            motivoReserva,
            descricao,
            numPessoas,
            status,
            data,
            somenteFuncionarios,
            created,
            modified,
            localId,
            associadoId,
            departamentoId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReservaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (motivoReserva != null ? "motivoReserva=" + motivoReserva + ", " : "") +
            (descricao != null ? "descricao=" + descricao + ", " : "") +
            (numPessoas != null ? "numPessoas=" + numPessoas + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (data != null ? "data=" + data + ", " : "") +
            (somenteFuncionarios != null ? "somenteFuncionarios=" + somenteFuncionarios + ", " : "") +
            (created != null ? "created=" + created + ", " : "") +
            (modified != null ? "modified=" + modified + ", " : "") +
            (localId != null ? "localId=" + localId + ", " : "") +
            (associadoId != null ? "associadoId=" + associadoId + ", " : "") +
            (departamentoId != null ? "departamentoId=" + departamentoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
