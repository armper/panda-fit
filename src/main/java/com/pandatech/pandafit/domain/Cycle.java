package com.pandatech.pandafit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Cycle.
 */
@Document(collection = "cycle")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cycle")
public class Cycle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("reps")
    private Integer reps;

    @Field("volume")
    private Integer volume;

    @DBRef
    @Field("excercise")
    @JsonIgnoreProperties(value = { "cycles", "routine" }, allowSetters = true)
    private Excercise excercise;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Cycle id(String id) {
        this.id = id;
        return this;
    }

    public Integer getReps() {
        return this.reps;
    }

    public Cycle reps(Integer reps) {
        this.reps = reps;
        return this;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Integer getVolume() {
        return this.volume;
    }

    public Cycle volume(Integer volume) {
        this.volume = volume;
        return this;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Excercise getExcercise() {
        return this.excercise;
    }

    public Cycle excercise(Excercise excercise) {
        this.setExcercise(excercise);
        return this;
    }

    public void setExcercise(Excercise excercise) {
        this.excercise = excercise;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cycle)) {
            return false;
        }
        return id != null && id.equals(((Cycle) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cycle{" +
            "id=" + getId() +
            ", reps=" + getReps() +
            ", volume=" + getVolume() +
            "}";
    }
}
