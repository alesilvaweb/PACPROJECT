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
 * A IconsRedesSociais.
 */
@Entity
@Table(name = "icons_redes_sociais")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IconsRedesSociais implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "icon")
    private String icon;

    @Lob
    @Column(name = "imagem")
    private byte[] imagem;

    @Column(name = "imagem_content_type")
    private String imagemContentType;

    @OneToMany(mappedBy = "icon")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "icon", "convenio" }, allowSetters = true)
    private Set<RedesSociaisConvenio> redeSocials = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IconsRedesSociais id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public IconsRedesSociais nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public IconsRedesSociais descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIcon() {
        return this.icon;
    }

    public IconsRedesSociais icon(String icon) {
        this.setIcon(icon);
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public byte[] getImagem() {
        return this.imagem;
    }

    public IconsRedesSociais imagem(byte[] imagem) {
        this.setImagem(imagem);
        return this;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public String getImagemContentType() {
        return this.imagemContentType;
    }

    public IconsRedesSociais imagemContentType(String imagemContentType) {
        this.imagemContentType = imagemContentType;
        return this;
    }

    public void setImagemContentType(String imagemContentType) {
        this.imagemContentType = imagemContentType;
    }

    public Set<RedesSociaisConvenio> getRedeSocials() {
        return this.redeSocials;
    }

    public void setRedeSocials(Set<RedesSociaisConvenio> redesSociaisConvenios) {
        if (this.redeSocials != null) {
            this.redeSocials.forEach(i -> i.setIcon(null));
        }
        if (redesSociaisConvenios != null) {
            redesSociaisConvenios.forEach(i -> i.setIcon(this));
        }
        this.redeSocials = redesSociaisConvenios;
    }

    public IconsRedesSociais redeSocials(Set<RedesSociaisConvenio> redesSociaisConvenios) {
        this.setRedeSocials(redesSociaisConvenios);
        return this;
    }

    public IconsRedesSociais addRedeSocial(RedesSociaisConvenio redesSociaisConvenio) {
        this.redeSocials.add(redesSociaisConvenio);
        redesSociaisConvenio.setIcon(this);
        return this;
    }

    public IconsRedesSociais removeRedeSocial(RedesSociaisConvenio redesSociaisConvenio) {
        this.redeSocials.remove(redesSociaisConvenio);
        redesSociaisConvenio.setIcon(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IconsRedesSociais)) {
            return false;
        }
        return id != null && id.equals(((IconsRedesSociais) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IconsRedesSociais{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", icon='" + getIcon() + "'" +
            ", imagem='" + getImagem() + "'" +
            ", imagemContentType='" + getImagemContentType() + "'" +
            "}";
    }
}
