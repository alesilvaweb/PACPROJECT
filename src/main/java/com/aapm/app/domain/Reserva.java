package com.aapm.app.domain;

import com.aapm.app.domain.enumeration.StatusReserva;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reserva.
 */
@Entity
@Table(name = "reserva")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "motivo_reserva", nullable = false)
    private String motivoReserva;

    @Column(name = "descricao")
    private String descricao;

    @NotNull
    @Column(name = "num_pessoas", nullable = false)
    private Integer numPessoas;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusReserva status;

    @NotNull
    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "somente_funcionarios")
    private Boolean somenteFuncionarios;

    @Column(name = "created")
    private Instant created;

    @Column(name = "modified")
    private Instant modified;

    @ManyToOne
    @JsonIgnoreProperties(value = { "reservas" }, allowSetters = true)
    private Local local;

    @ManyToOne
    @JsonIgnoreProperties(value = { "reservas", "contatos", "dependentes" }, allowSetters = true)
    private Associado associado;

    @ManyToOne
    @JsonIgnoreProperties(value = { "reservas" }, allowSetters = true)
    private Departamento departamento;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reserva id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotivoReserva() {
        return this.motivoReserva;
    }

    public Reserva motivoReserva(String motivoReserva) {
        this.setMotivoReserva(motivoReserva);
        return this;
    }

    public void setMotivoReserva(String motivoReserva) {
        this.motivoReserva = motivoReserva;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Reserva descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getNumPessoas() {
        return this.numPessoas;
    }

    public Reserva numPessoas(Integer numPessoas) {
        this.setNumPessoas(numPessoas);
        return this;
    }

    public void setNumPessoas(Integer numPessoas) {
        this.numPessoas = numPessoas;
    }

    public StatusReserva getStatus() {
        return this.status;
    }

    public Reserva status(StatusReserva status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(StatusReserva status) {
        this.status = status;
    }

    public LocalDate getData() {
        return this.data;
    }

    public Reserva data(LocalDate data) {
        this.setData(data);
        return this;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Boolean getSomenteFuncionarios() {
        return this.somenteFuncionarios;
    }

    public Reserva somenteFuncionarios(Boolean somenteFuncionarios) {
        this.setSomenteFuncionarios(somenteFuncionarios);
        return this;
    }

    public void setSomenteFuncionarios(Boolean somenteFuncionarios) {
        this.somenteFuncionarios = somenteFuncionarios;
    }

    public Instant getCreated() {
        return this.created;
    }

    public Reserva created(Instant created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getModified() {
        return this.modified;
    }

    public Reserva modified(Instant modified) {
        this.setModified(modified);
        return this;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    public Local getLocal() {
        return this.local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public Reserva local(Local local) {
        this.setLocal(local);
        return this;
    }

    public Associado getAssociado() {
        return this.associado;
    }

    public void setAssociado(Associado associado) {
        this.associado = associado;
    }

    public Reserva associado(Associado associado) {
        this.setAssociado(associado);
        return this;
    }

    public Departamento getDepartamento() {
        return this.departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Reserva departamento(Departamento departamento) {
        this.setDepartamento(departamento);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reserva)) {
            return false;
        }
        return id != null && id.equals(((Reserva) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reserva{" +
            "id=" + getId() +
            ", motivoReserva='" + getMotivoReserva() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", numPessoas=" + getNumPessoas() +
            ", status='" + getStatus() + "'" +
            ", data='" + getData() + "'" +
            ", somenteFuncionarios='" + getSomenteFuncionarios() + "'" +
            ", created='" + getCreated() + "'" +
            ", modified='" + getModified() + "'" +
            "}";
    }
}
