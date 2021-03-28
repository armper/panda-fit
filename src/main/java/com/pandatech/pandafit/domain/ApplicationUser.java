package com.pandatech.pandafit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ApplicationUser.
 */
@Document(collection = "application_user")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "applicationuser")
public class ApplicationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("ranking")
    private Integer ranking;

    @DBRef
    @Field("internalUser")
    private User internalUser;

    @DBRef
    @Field("routines")
    @JsonIgnoreProperties(value = { "excercises", "users" }, allowSetters = true)
    private Set<Routine> routines = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ApplicationUser id(String id) {
        this.id = id;
        return this;
    }

    public Integer getRanking() {
        return this.ranking;
    }

    public ApplicationUser ranking(Integer ranking) {
        this.ranking = ranking;
        return this;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public ApplicationUser internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public Set<Routine> getRoutines() {
        return this.routines;
    }

    public ApplicationUser routines(Set<Routine> routines) {
        this.setRoutines(routines);
        return this;
    }

    public ApplicationUser addRoutine(Routine routine) {
        this.routines.add(routine);
        routine.getUsers().add(this);
        return this;
    }

    public ApplicationUser removeRoutine(Routine routine) {
        this.routines.remove(routine);
        routine.getUsers().remove(this);
        return this;
    }

    public void setRoutines(Set<Routine> routines) {
        this.routines = routines;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUser)) {
            return false;
        }
        return id != null && id.equals(((ApplicationUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUser{" +
            "id=" + getId() +
            ", ranking=" + getRanking() +
            "}";
    }
}
