package com.aapm.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.aapm.app.domain.User} entity. This class is used
 * in {@link com.aapm.app.web.rest.UserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /associados?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;
    private StringFilter login;
    private StringFilter firstName;
    private StringFilter password;
    private StringFilter lastName;
    private StringFilter email;
    private StringFilter imageUrl;
    private StringFilter activationKey;
    private StringFilter resetKey;
    private StringFilter langKey;
    private Boolean activated;
    private InstantFilter resetDate;
    private InstantFilter created;
    private InstantFilter modified;

    public UserCriteria() {}

    public UserCriteria(UserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.login = other.login == null ? null : other.login.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.password = other.password == null ? null : other.password.copy();
        this.activated = other.activated;
        this.imageUrl = other.imageUrl == null ? null : other.imageUrl.copy();
        this.activationKey = other.activationKey == null ? null : other.activationKey.copy();
        this.resetKey = other.resetKey == null ? null : other.resetKey.copy();
        this.langKey = other.langKey == null ? null : other.langKey.copy();
        this.resetDate = other.resetDate == null ? null : other.resetDate.copy();
        this.created = other.created == null ? null : other.created.copy();
        this.modified = other.modified == null ? null : other.modified.copy();
    }

    @Override
    public UserCriteria copy() {
        return new UserCriteria(this);
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

    public StringFilter getLangKey() {
        return langKey;
    }

    public void setLangKey(StringFilter langKey) {
        this.langKey = langKey;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLogin() {
        return login;
    }

    public StringFilter login() {
        if (login == null) {
            login = new StringFilter();
        }
        return login;
    }

    public void setLogin(StringFilter login) {
        this.login = login;
    }

    public StringFilter getFirstName() {
        return login;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPassword() {
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
    }

    public StringFilter getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(StringFilter activationKey) {
        this.activationKey = activationKey;
    }

    public StringFilter getResetKey() {
        return resetKey;
    }

    public void setResetKey(StringFilter resetKey) {
        this.resetKey = resetKey;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
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

    public Boolean getDistinct() {
        return activated;
    }

    public void setDistinct(Boolean distinct) {
        this.activated = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserCriteria that = (UserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(login, that.login) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(langKey, that.langKey) &&
            Objects.equals(resetDate, that.resetDate) &&
            Objects.equals(resetKey, that.resetKey) &&
            Objects.equals(activationKey, that.activationKey) &&
            Objects.equals(created, that.created) &&
            Objects.equals(modified, that.modified)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, firstName, email, created, modified);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssociadoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (login != null ? "login=" + login + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (created != null ? "created=" + created + ", " : "") +
            (modified != null ? "modified=" + modified + ", " : "") +
            (activated != null ? "distinct=" + activated + ", " : "") +
            "}";
    }

    public InstantFilter getResetDate() {
        return resetDate;
    }

    public void setResetDate(InstantFilter resetDate) {
        this.resetDate = resetDate;
    }
}
