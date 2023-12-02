package com.aapm.app.domain;

import com.aapm.app.domain.enumeration.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Local.
 */
@Entity
@Table(name = "local")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Local implements Serializable {

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

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "descricao", nullable = false)
    private String descricao;

    @NotNull
    @Column(name = "capacidade", nullable = false)
    private Integer capacidade;

    @Lob
    @Column(name = "imagen", nullable = false)
    private byte[] imagen;

    @NotNull
    @Column(name = "imagen_content_type", nullable = false)
    private String imagenContentType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "observacoes")
    private String observacoes;

    @Column(name = "localizacao")
    private String localizacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @NotNull
    @Column(name = "valor", nullable = false)
    private Float valor;

    @Column(name = "cor")
    private String cor;

    @Column(name = "created")
    private Instant created;

    @Column(name = "modified")
    private Instant modified;

    @OneToMany(mappedBy = "local")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "local", "associado", "departamento" }, allowSetters = true)
    private Set<Reserva> reservas = new HashSet<>();

    public Local() {}

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Local id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Local nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Local descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getCapacidade() {
        return this.capacidade;
    }

    public Local capacidade(Integer capacidade) {
        this.setCapacidade(capacidade);
        return this;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public byte[] getImagen() {
        return this.imagen;
    }

    public Local imagen(byte[] imagen) {
        this.setImagen(imagen);
        return this;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getImagenContentType() {
        return this.imagenContentType;
    }

    public Local imagenContentType(String imagenContentType) {
        this.imagenContentType = imagenContentType;
        return this;
    }

    public void setImagenContentType(String imagenContentType) {
        this.imagenContentType = imagenContentType;
    }

    public String getObservacoes() {
        return this.observacoes;
    }

    public Local observacoes(String observacoes) {
        this.setObservacoes(observacoes);
        return this;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getLocalizacao() {
        return this.localizacao;
    }

    public Local localizacao(String localizacao) {
        this.setLocalizacao(localizacao);
        return this;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public Status getStatus() {
        return this.status;
    }

    public Local status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Float getValor() {
        return this.valor;
    }

    public Local valor(Float valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public String getCor() {
        return this.cor;
    }

    public Local cor(String cor) {
        this.setCor(cor);
        return this;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Instant getCreated() {
        return this.created;
    }

    public Local created(Instant created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getModified() {
        return this.modified;
    }

    public Local modified(Instant modified) {
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
            this.reservas.forEach(i -> i.setLocal(null));
        }
        if (reservas != null) {
            reservas.forEach(i -> i.setLocal(this));
        }
        this.reservas = reservas;
    }

    public Local reservas(Set<Reserva> reservas) {
        this.setReservas(reservas);
        return this;
    }

    public Local addReserva(Reserva reserva) {
        this.reservas.add(reserva);
        reserva.setLocal(this);
        return this;
    }

    public Local removeReserva(Reserva reserva) {
        this.reservas.remove(reserva);
        reserva.setLocal(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Local)) {
            return false;
        }
        return id != null && id.equals(((Local) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Local{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", capacidade=" + getCapacidade() +
            ", imagen='" + getImagen() + "'" +
            ", imagenContentType='" + getImagenContentType() + "'" +
            ", observacoes='" + getObservacoes() + "'" +
            ", localizacao='" + getLocalizacao() + "'" +
            ", status='" + getStatus() + "'" +
            ", valor=" + getValor() +
            ", cor='" + getCor() + "'" +
            ", created='" + getCreated() + "'" +
            ", modified='" + getModified() + "'" +
            "}";
    }
}
