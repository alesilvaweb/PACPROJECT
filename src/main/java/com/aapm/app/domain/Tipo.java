package com.aapm.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tipo.
 */
@Entity
@Table(name = "tipo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tipo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @NotNull
    @Column(name = "chave", nullable = false)
    private String chave;

    @OneToMany(mappedBy = "tipo")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tipo" }, allowSetters = true)
    private Set<Mensagem> mensagems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tipo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return this.tipo;
    }

    public Tipo tipo(String tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getChave() {
        return this.chave;
    }

    public Tipo chave(String chave) {
        this.setChave(chave);
        return this;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public Set<Mensagem> getMensagems() {
        return this.mensagems;
    }

    public void setMensagems(Set<Mensagem> mensagems) {
        if (this.mensagems != null) {
            this.mensagems.forEach(i -> i.setTipo(null));
        }
        if (mensagems != null) {
            mensagems.forEach(i -> i.setTipo(this));
        }
        this.mensagems = mensagems;
    }

    public Tipo mensagems(Set<Mensagem> mensagems) {
        this.setMensagems(mensagems);
        return this;
    }

    public Tipo addMensagem(Mensagem mensagem) {
        this.mensagems.add(mensagem);
        mensagem.setTipo(this);
        return this;
    }

    public Tipo removeMensagem(Mensagem mensagem) {
        this.mensagems.remove(mensagem);
        mensagem.setTipo(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tipo)) {
            return false;
        }
        return id != null && id.equals(((Tipo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tipo{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", chave='" + getChave() + "'" +
            "}";
    }
}
