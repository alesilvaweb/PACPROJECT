package com.aapm.app.service.dto;

import com.aapm.app.domain.enumeration.StatusReserva;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.aapm.app.domain.Reserva} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReservaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2)
    private String motivoReserva;

    private String descricao;

    @NotNull
    private Integer numPessoas;

    private StatusReserva status;

    @NotNull
    private LocalDate data;

    private Boolean somenteFuncionarios;

    private Instant created;

    private Instant modified;

    private LocalDTO local;

    private AssociadoDTO associado;

    private DepartamentoDTO departamento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotivoReserva() {
        return motivoReserva;
    }

    public void setMotivoReserva(String motivoReserva) {
        this.motivoReserva = motivoReserva;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getNumPessoas() {
        return numPessoas;
    }

    public void setNumPessoas(Integer numPessoas) {
        this.numPessoas = numPessoas;
    }

    public StatusReserva getStatus() {
        return status;
    }

    public void setStatus(StatusReserva status) {
        this.status = status;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Boolean getSomenteFuncionarios() {
        return somenteFuncionarios;
    }

    public void setSomenteFuncionarios(Boolean somenteFuncionarios) {
        this.somenteFuncionarios = somenteFuncionarios;
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

    public LocalDTO getLocal() {
        return local;
    }

    public void setLocal(LocalDTO local) {
        this.local = local;
    }

    public AssociadoDTO getAssociado() {
        return associado;
    }

    public void setAssociado(AssociadoDTO associado) {
        this.associado = associado;
    }

    public DepartamentoDTO getDepartamento() {
        return departamento;
    }

    public void setDepartamento(DepartamentoDTO departamento) {
        this.departamento = departamento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservaDTO)) {
            return false;
        }

        ReservaDTO reservaDTO = (ReservaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reservaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReservaDTO{" +
            "id=" + getId() +
            ", motivoReserva='" + getMotivoReserva() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", numPessoas=" + getNumPessoas() +
            ", status='" + getStatus() + "'" +
            ", data='" + getData() + "'" +
            ", somenteFuncionarios='" + getSomenteFuncionarios() + "'" +
            ", created='" + getCreated() + "'" +
            ", modified='" + getModified() + "'" +
            ", local=" + getLocal() +
            ", associado=" + getAssociado() +
            ", departamento=" + getDepartamento() +
            "}";
    }
}
