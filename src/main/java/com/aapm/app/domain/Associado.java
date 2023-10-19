package com.aapm.app.domain;

import com.aapm.app.domain.enumeration.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Associado.
 */
@Entity
@Table(name = "associado")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Associado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "matricula", nullable = false)
    private String matricula;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "telefone")
    private String telefone;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "created")
    private Instant created;

    @Column(name = "modified")
    private Instant modified;

    @OneToMany(mappedBy = "associado")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "local", "associado", "departamento" }, allowSetters = true)
    private Set<Reserva> reservas = new HashSet<>();

    @OneToMany(mappedBy = "associado")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "associado" }, allowSetters = true)
    private Set<Contato> contatos = new HashSet<>();

    @OneToMany(mappedBy = "associado")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "associado" }, allowSetters = true)
    private Set<Dependente> dependentes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Associado id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Associado nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return this.matricula;
    }

    public Associado matricula(String matricula) {
        this.setMatricula(matricula);
        return this;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Status getStatus() {
        return this.status;
    }

    public Associado status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public Associado telefone(String telefone) {
        this.setTelefone(telefone);
        return this;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return this.email;
    }

    public Associado email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataNascimento() {
        return this.dataNascimento;
    }

    public Associado dataNascimento(LocalDate dataNascimento) {
        this.setDataNascimento(dataNascimento);
        return this;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Instant getCreated() {
        return this.created;
    }

    public Associado created(Instant created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getModified() {
        return this.modified;
    }

    public Associado modified(Instant modified) {
        this.setModified(modified);
        return this;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    public Set<Reserva> getReservas() {
        return this.reservas;
    }

    public void setReservas(Set<Reserva> reservas) {
        if (this.reservas != null) {
            this.reservas.forEach(i -> i.setAssociado(null));
        }
        if (reservas != null) {
            reservas.forEach(i -> i.setAssociado(this));
        }
        this.reservas = reservas;
    }

    public Associado reservas(Set<Reserva> reservas) {
        this.setReservas(reservas);
        return this;
    }

    public Associado addReserva(Reserva reserva) {
        this.reservas.add(reserva);
        reserva.setAssociado(this);
        return this;
    }

    public Associado removeReserva(Reserva reserva) {
        this.reservas.remove(reserva);
        reserva.setAssociado(null);
        return this;
    }

    public Set<Contato> getContatos() {
        return this.contatos;
    }

    public void setContatos(Set<Contato> contatoes) {
        if (this.contatos != null) {
            this.contatos.forEach(i -> i.setAssociado(null));
        }
        if (contatoes != null) {
            contatoes.forEach(i -> i.setAssociado(this));
        }
        this.contatos = contatoes;
    }

    public Associado contatos(Set<Contato> contatoes) {
        this.setContatos(contatoes);
        return this;
    }

    public Associado addContatos(Contato contato) {
        this.contatos.add(contato);
        contato.setAssociado(this);
        return this;
    }

    public Associado removeContatos(Contato contato) {
        this.contatos.remove(contato);
        contato.setAssociado(null);
        return this;
    }

    public Set<Dependente> getDependentes() {
        return this.dependentes;
    }

    public void setDependentes(Set<Dependente> dependentes) {
        if (this.dependentes != null) {
            this.dependentes.forEach(i -> i.setAssociado(null));
        }
        if (dependentes != null) {
            dependentes.forEach(i -> i.setAssociado(this));
        }
        this.dependentes = dependentes;
    }

    public Associado dependentes(Set<Dependente> dependentes) {
        this.setDependentes(dependentes);
        return this;
    }

    public Associado addDependentes(Dependente dependente) {
        this.dependentes.add(dependente);
        dependente.setAssociado(this);
        return this;
    }

    public Associado removeDependentes(Dependente dependente) {
        this.dependentes.remove(dependente);
        dependente.setAssociado(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Associado)) {
            return false;
        }
        return id != null && id.equals(((Associado) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Associado{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", matricula='" + getMatricula() + "'" +
            ", status='" + getStatus() + "'" +
            ", telefone='" + getTelefone() + "'" +
            ", email='" + getEmail() + "'" +
            ", dataNascimento='" + getDataNascimento() + "'" +
            ", created='" + getCreated() + "'" +
            ", modified='" + getModified() + "'" +
            "}";
    }
}
