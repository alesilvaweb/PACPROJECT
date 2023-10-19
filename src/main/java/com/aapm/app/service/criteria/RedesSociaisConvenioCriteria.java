package com.aapm.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.aapm.app.domain.RedesSociaisConvenio} entity. This class is used
 * in {@link com.aapm.app.web.rest.RedesSociaisConvenioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /redes-sociais-convenios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RedesSociaisConvenioCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter descricao;

    private StringFilter endereco;

    private InstantFilter created;

    private InstantFilter modified;

    private LongFilter iconId;

    private LongFilter convenioId;

    private Boolean distinct;

    public RedesSociaisConvenioCriteria() {}

    public RedesSociaisConvenioCriteria(RedesSociaisConvenioCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
        this.endereco = other.endereco == null ? null : other.endereco.copy();
        this.created = other.created == null ? null : other.created.copy();
        this.modified = other.modified == null ? null : other.modified.copy();
        this.iconId = other.iconId == null ? null : other.iconId.copy();
        this.convenioId = other.convenioId == null ? null : other.convenioId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RedesSociaisConvenioCriteria copy() {
        return new RedesSociaisConvenioCriteria(this);
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

    public StringFilter getEndereco() {
        return endereco;
    }

    public StringFilter endereco() {
        if (endereco == null) {
            endereco = new StringFilter();
        }
        return endereco;
    }

    public void setEndereco(StringFilter endereco) {
        this.endereco = endereco;
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

    public LongFilter getIconId() {
        return iconId;
    }

    public LongFilter iconId() {
        if (iconId == null) {
            iconId = new LongFilter();
        }
        return iconId;
    }

    public void setIconId(LongFilter iconId) {
        this.iconId = iconId;
    }

    public LongFilter getConvenioId() {
        return convenioId;
    }

    public LongFilter convenioId() {
        if (convenioId == null) {
            convenioId = new LongFilter();
        }
        return convenioId;
    }

    public void setConvenioId(LongFilter convenioId) {
        this.convenioId = convenioId;
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
        final RedesSociaisConvenioCriteria that = (RedesSociaisConvenioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(endereco, that.endereco) &&
            Objects.equals(created, that.created) &&
            Objects.equals(modified, that.modified) &&
            Objects.equals(iconId, that.iconId) &&
            Objects.equals(convenioId, that.convenioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, descricao, endereco, created, modified, iconId, convenioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RedesSociaisConvenioCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (descricao != null ? "descricao=" + descricao + ", " : "") +
            (endereco != null ? "endereco=" + endereco + ", " : "") +
            (created != null ? "created=" + created + ", " : "") +
            (modified != null ? "modified=" + modified + ", " : "") +
            (iconId != null ? "iconId=" + iconId + ", " : "") +
            (convenioId != null ? "convenioId=" + convenioId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
