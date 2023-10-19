package com.aapm.app.service.criteria;

import com.aapm.app.domain.enumeration.TipoContato;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.aapm.app.domain.Contato} entity. This class is used
 * in {@link com.aapm.app.web.rest.ContatoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contatoes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContatoCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoContato
     */
    public static class TipoContatoFilter extends Filter<TipoContato> {

        public TipoContatoFilter() {}

        public TipoContatoFilter(TipoContatoFilter filter) {
            super(filter);
        }

        @Override
        public TipoContatoFilter copy() {
            return new TipoContatoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TipoContatoFilter tipo;

    private StringFilter contato;

    private InstantFilter created;

    private InstantFilter modified;

    private LongFilter associadoId;

    private Boolean distinct;

    public ContatoCriteria() {}

    public ContatoCriteria(ContatoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tipo = other.tipo == null ? null : other.tipo.copy();
        this.contato = other.contato == null ? null : other.contato.copy();
        this.created = other.created == null ? null : other.created.copy();
        this.modified = other.modified == null ? null : other.modified.copy();
        this.associadoId = other.associadoId == null ? null : other.associadoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ContatoCriteria copy() {
        return new ContatoCriteria(this);
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

    public TipoContatoFilter getTipo() {
        return tipo;
    }

    public TipoContatoFilter tipo() {
        if (tipo == null) {
            tipo = new TipoContatoFilter();
        }
        return tipo;
    }

    public void setTipo(TipoContatoFilter tipo) {
        this.tipo = tipo;
    }

    public StringFilter getContato() {
        return contato;
    }

    public StringFilter contato() {
        if (contato == null) {
            contato = new StringFilter();
        }
        return contato;
    }

    public void setContato(StringFilter contato) {
        this.contato = contato;
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

    public LongFilter getAssociadoId() {
        return associadoId;
    }

    public LongFilter associadoId() {
        if (associadoId == null) {
            associadoId = new LongFilter();
        }
        return associadoId;
    }

    public void setAssociadoId(LongFilter associadoId) {
        this.associadoId = associadoId;
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
        final ContatoCriteria that = (ContatoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(contato, that.contato) &&
            Objects.equals(created, that.created) &&
            Objects.equals(modified, that.modified) &&
            Objects.equals(associadoId, that.associadoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipo, contato, created, modified, associadoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContatoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tipo != null ? "tipo=" + tipo + ", " : "") +
            (contato != null ? "contato=" + contato + ", " : "") +
            (created != null ? "created=" + created + ", " : "") +
            (modified != null ? "modified=" + modified + ", " : "") +
            (associadoId != null ? "associadoId=" + associadoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
