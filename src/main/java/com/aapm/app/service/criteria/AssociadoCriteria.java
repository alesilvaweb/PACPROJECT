package com.aapm.app.service.criteria;

import com.aapm.app.domain.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.aapm.app.domain.Associado} entity. This class is used
 * in {@link com.aapm.app.web.rest.AssociadoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /associados?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssociadoCriteria implements Serializable, Criteria {

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

    private StringFilter matricula;

    private StatusFilter status;

    private StringFilter telefone;

    private StringFilter email;

    private LocalDateFilter dataNascimento;

    private InstantFilter created;

    private InstantFilter modified;

    private LongFilter reservaId;

    private LongFilter contatosId;

    private LongFilter dependentesId;

    private Boolean distinct;

    public AssociadoCriteria() {}

    public AssociadoCriteria(AssociadoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.matricula = other.matricula == null ? null : other.matricula.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.telefone = other.telefone == null ? null : other.telefone.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.dataNascimento = other.dataNascimento == null ? null : other.dataNascimento.copy();
        this.created = other.created == null ? null : other.created.copy();
        this.modified = other.modified == null ? null : other.modified.copy();
        this.reservaId = other.reservaId == null ? null : other.reservaId.copy();
        this.contatosId = other.contatosId == null ? null : other.contatosId.copy();
        this.dependentesId = other.dependentesId == null ? null : other.dependentesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AssociadoCriteria copy() {
        return new AssociadoCriteria(this);
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

    public StringFilter getMatricula() {
        return matricula;
    }

    public StringFilter matricula() {
        if (matricula == null) {
            matricula = new StringFilter();
        }
        return matricula;
    }

    public void setMatricula(StringFilter matricula) {
        this.matricula = matricula;
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

    public LocalDateFilter getDataNascimento() {
        return dataNascimento;
    }

    public LocalDateFilter dataNascimento() {
        if (dataNascimento == null) {
            dataNascimento = new LocalDateFilter();
        }
        return dataNascimento;
    }

    public void setDataNascimento(LocalDateFilter dataNascimento) {
        this.dataNascimento = dataNascimento;
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

    public LongFilter getContatosId() {
        return contatosId;
    }

    public LongFilter contatosId() {
        if (contatosId == null) {
            contatosId = new LongFilter();
        }
        return contatosId;
    }

    public void setContatosId(LongFilter contatosId) {
        this.contatosId = contatosId;
    }

    public LongFilter getDependentesId() {
        return dependentesId;
    }

    public LongFilter dependentesId() {
        if (dependentesId == null) {
            dependentesId = new LongFilter();
        }
        return dependentesId;
    }

    public void setDependentesId(LongFilter dependentesId) {
        this.dependentesId = dependentesId;
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
        final AssociadoCriteria that = (AssociadoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(matricula, that.matricula) &&
            Objects.equals(status, that.status) &&
            Objects.equals(telefone, that.telefone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(dataNascimento, that.dataNascimento) &&
            Objects.equals(created, that.created) &&
            Objects.equals(modified, that.modified) &&
            Objects.equals(reservaId, that.reservaId) &&
            Objects.equals(contatosId, that.contatosId) &&
            Objects.equals(dependentesId, that.dependentesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nome,
            matricula,
            status,
            telefone,
            email,
            dataNascimento,
            created,
            modified,
            reservaId,
            contatosId,
            dependentesId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssociadoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (matricula != null ? "matricula=" + matricula + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (telefone != null ? "telefone=" + telefone + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (dataNascimento != null ? "dataNascimento=" + dataNascimento + ", " : "") +
            (created != null ? "created=" + created + ", " : "") +
            (modified != null ? "modified=" + modified + ", " : "") +
            (reservaId != null ? "reservaId=" + reservaId + ", " : "") +
            (contatosId != null ? "contatosId=" + contatosId + ", " : "") +
            (dependentesId != null ? "dependentesId=" + dependentesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
