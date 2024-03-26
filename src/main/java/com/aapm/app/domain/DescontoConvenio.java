package com.aapm.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DescontoConvenio.
 */
@Entity
@Table(name = "desconto_convenio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DescontoConvenio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "desconto")
    private String desconto;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "tipo_desconto")
    private String tipo;

    @ManyToOne
    @JsonIgnoreProperties(value = { "imagens", "redesSociais", "descontos", "categoria" }, allowSetters = true)
    private Convenio convenio;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DescontoConvenio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesconto() {
        return this.desconto;
    }

    public DescontoConvenio desconto(String desconto) {
        this.setDesconto(desconto);
        return this;
    }

    public void setDesconto(String desconto) {
        this.desconto = desconto;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public DescontoConvenio descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Convenio getConvenio() {
        return this.convenio;
    }

    public void setConvenio(Convenio convenio) {
        this.convenio = convenio;
    }

    public DescontoConvenio convenio(Convenio convenio) {
        this.setConvenio(convenio);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DescontoConvenio)) {
            return false;
        }
        return id != null && id.equals(((DescontoConvenio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DescontoConvenio{" +
            "id=" + getId() +
            ", desconto='" + getDesconto() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
