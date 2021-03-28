package com.pandatech.pandafit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pandatech.pandafit.domain.enumeration.ExcerciseType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Excercise.
 */
@Document(collection = "excercise")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "excercise")
public class Excercise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("type")
    private ExcerciseType type;

    @Field("current_volume")
    private Integer currentVolume;

    @Field("starting_volume")
    private Integer startingVolume;

    @Field("goal_volume")
    private Integer goalVolume;

    @DBRef
    @Field("cycle")
    @JsonIgnoreProperties(value = { "excercise" }, allowSetters = true)
    private Set<Cycle> cycles = new HashSet<>();

    @DBRef
    @Field("routine")
    @JsonIgnoreProperties(value = { "excercises", "users" }, allowSetters = true)
    private Routine routine;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Excercise id(String id) {
        this.id = id;
        return this;
    }

    public ExcerciseType getType() {
        return this.type;
    }

    public Excercise type(ExcerciseType type) {
        this.type = type;
        return this;
    }

    public void setType(ExcerciseType type) {
        this.type = type;
    }

    public Integer getCurrentVolume() {
        return this.currentVolume;
    }

    public Excercise currentVolume(Integer currentVolume) {
        this.currentVolume = currentVolume;
        return this;
    }

    public void setCurrentVolume(Integer currentVolume) {
        this.currentVolume = currentVolume;
    }

    public Integer getStartingVolume() {
        return this.startingVolume;
    }

    public Excercise startingVolume(Integer startingVolume) {
        this.startingVolume = startingVolume;
        return this;
    }

    public void setStartingVolume(Integer startingVolume) {
        this.startingVolume = startingVolume;
    }

    public Integer getGoalVolume() {
        return this.goalVolume;
    }

    public Excercise goalVolume(Integer goalVolume) {
        this.goalVolume = goalVolume;
        return this;
    }

    public void setGoalVolume(Integer goalVolume) {
        this.goalVolume = goalVolume;
    }

    public Set<Cycle> getCycles() {
        return this.cycles;
    }

    public Excercise cycles(Set<Cycle> cycles) {
        this.setCycles(cycles);
        return this;
    }

    public Excercise addCycle(Cycle cycle) {
        this.cycles.add(cycle);
        cycle.setExcercise(this);
        return this;
    }

    public Excercise removeCycle(Cycle cycle) {
        this.cycles.remove(cycle);
        cycle.setExcercise(null);
        return this;
    }

    public void setCycles(Set<Cycle> cycles) {
        if (this.cycles != null) {
            this.cycles.forEach(i -> i.setExcercise(null));
        }
        if (cycles != null) {
            cycles.forEach(i -> i.setExcercise(this));
        }
        this.cycles = cycles;
    }

    public Routine getRoutine() {
        return this.routine;
    }

    public Excercise routine(Routine routine) {
        this.setRoutine(routine);
        return this;
    }

    public void setRoutine(Routine routine) {
        this.routine = routine;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Excercise)) {
            return false;
        }
        return id != null && id.equals(((Excercise) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Excercise{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", currentVolume=" + getCurrentVolume() +
            ", startingVolume=" + getStartingVolume() +
            ", goalVolume=" + getGoalVolume() +
            "}";
    }
}
