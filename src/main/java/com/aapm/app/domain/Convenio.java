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
 * A Convenio.
 */
@Entity
@Table(name = "convenio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Convenio implements Serializable {

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

    @Column(name = "titulo")
    private String titulo;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "descricao")
    private String descricao;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "email")
    private String email;

    @Lob
    @Column(name = "imagem", nullable = false)
    private byte[] imagem;

    @NotNull
    @Column(name = "imagem_content_type", nullable = false)
    private String imagemContentType;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @Lob
    @Column(name = "banner")
    private byte[] banner;

    @Column(name = "banner_content_type")
    private String bannerContentType;

    @Column(name = "localizacao")
    private String localizacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "created")
    private Instant created;

    @Column(name = "modified")
    private Instant modified;

    @OneToMany(mappedBy = "convenio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "convenio" }, allowSetters = true)
    private Set<ImagensConvenio> imagens = new HashSet<>();

    @OneToMany(mappedBy = "convenio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "icon", "convenio" }, allowSetters = true)
    private Set<RedesSociaisConvenio> redesSociais = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "convenios" }, allowSetters = true)
    private Categoria categoria;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Convenio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Convenio nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Convenio titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Convenio descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEndereco() {
        return this.endereco;
    }

    public Convenio endereco(String endereco) {
        this.setEndereco(endereco);
        return this;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public Convenio telefone(String telefone) {
        this.setTelefone(telefone);
        return this;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return this.email;
    }

    public Convenio email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getImagem() {
        return this.imagem;
    }

    public Convenio imagem(byte[] imagem) {
        this.setImagem(imagem);
        return this;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public String getImagemContentType() {
        return this.imagemContentType;
    }

    public Convenio imagemContentType(String imagemContentType) {
        this.imagemContentType = imagemContentType;
        return this;
    }

    public void setImagemContentType(String imagemContentType) {
        this.imagemContentType = imagemContentType;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public Convenio logo(byte[] logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return this.logoContentType;
    }

    public Convenio logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public byte[] getBanner() {
        return this.banner;
    }

    public Convenio banner(byte[] banner) {
        this.setBanner(banner);
        return this;
    }

    public void setBanner(byte[] banner) {
        this.banner = banner;
    }

    public String getBannerContentType() {
        return this.bannerContentType;
    }

    public Convenio bannerContentType(String bannerContentType) {
        this.bannerContentType = bannerContentType;
        return this;
    }

    public void setBannerContentType(String bannerContentType) {
        this.bannerContentType = bannerContentType;
    }

    public String getLocalizacao() {
        return this.localizacao;
    }

    public Convenio localizacao(String localizacao) {
        this.setLocalizacao(localizacao);
        return this;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public Status getStatus() {
        return this.status;
    }

    public Convenio status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getCreated() {
        return this.created;
    }

    public Convenio created(Instant created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getModified() {
        return this.modified;
    }

    public Convenio modified(Instant modified) {
        this.setModified(modified);
        return this;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    public Set<ImagensConvenio> getImagens() {
        return this.imagens;
    }

    public void setImagens(Set<ImagensConvenio> imagensConvenios) {
        if (this.imagens != null) {
            this.imagens.forEach(i -> i.setConvenio(null));
        }
        if (imagensConvenios != null) {
            imagensConvenios.forEach(i -> i.setConvenio(this));
        }
        this.imagens = imagensConvenios;
    }

    public Convenio imagens(Set<ImagensConvenio> imagensConvenios) {
        this.setImagens(imagensConvenios);
        return this;
    }

    public Convenio addImagens(ImagensConvenio imagensConvenio) {
        this.imagens.add(imagensConvenio);
        imagensConvenio.setConvenio(this);
        return this;
    }

    public Convenio removeImagens(ImagensConvenio imagensConvenio) {
        this.imagens.remove(imagensConvenio);
        imagensConvenio.setConvenio(null);
        return this;
    }

    public Set<RedesSociaisConvenio> getRedesSociais() {
        return this.redesSociais;
    }

    public void setRedesSociais(Set<RedesSociaisConvenio> redesSociaisConvenios) {
        if (this.redesSociais != null) {
            this.redesSociais.forEach(i -> i.setConvenio(null));
        }
        if (redesSociaisConvenios != null) {
            redesSociaisConvenios.forEach(i -> i.setConvenio(this));
        }
        this.redesSociais = redesSociaisConvenios;
    }

    public Convenio redesSociais(Set<RedesSociaisConvenio> redesSociaisConvenios) {
        this.setRedesSociais(redesSociaisConvenios);
        return this;
    }

    public Convenio addRedesSociais(RedesSociaisConvenio redesSociaisConvenio) {
        this.redesSociais.add(redesSociaisConvenio);
        redesSociaisConvenio.setConvenio(this);
        return this;
    }

    public Convenio removeRedesSociais(RedesSociaisConvenio redesSociaisConvenio) {
        this.redesSociais.remove(redesSociaisConvenio);
        redesSociaisConvenio.setConvenio(null);
        return this;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Convenio categoria(Categoria categoria) {
        this.setCategoria(categoria);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Convenio)) {
            return false;
        }
        return id != null && id.equals(((Convenio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Convenio{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", titulo='" + getTitulo() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", endereco='" + getEndereco() + "'" +
            ", telefone='" + getTelefone() + "'" +
            ", email='" + getEmail() + "'" +
            ", imagem='" + getImagem() + "'" +
            ", imagemContentType='" + getImagemContentType() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            ", banner='" + getBanner() + "'" +
            ", bannerContentType='" + getBannerContentType() + "'" +
            ", localizacao='" + getLocalizacao() + "'" +
            ", status='" + getStatus() + "'" +
            ", created='" + getCreated() + "'" +
            ", modified='" + getModified() + "'" +
            "}";
    }
}
