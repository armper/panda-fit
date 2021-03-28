package com.pandatech.pandafit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Routine.
 */
@Document(collection = "routine")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "routine")
public class Routine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("date_started")
    private LocalDate dateStarted;

    @Field("date_ended")
    private LocalDate dateEnded;

    @Field("goal_date")
    private LocalDate goalDate;

    @Field("starting_body_weight")
    private Integer startingBodyWeight;

    @Field("ending_body_weight")
    private Integer endingBodyWeight;

    @DBRef
    @Field("excercise")
    @JsonIgnoreProperties(value = { "cycles", "routine" }, allowSetters = true)
    private Set<Excercise> excercises = new HashSet<>();

    @DBRef
    @Field("users")
    @JsonIgnoreProperties(value = { "internalUser", "routines" }, allowSetters = true)
    private Set<ApplicationUser> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Routine id(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Routine name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateStarted() {
        return this.dateStarted;
    }

    public Routine dateStarted(LocalDate dateStarted) {
        this.dateStarted = dateStarted;
        return this;
    }

    public void setDateStarted(LocalDate dateStarted) {
        this.dateStarted = dateStarted;
    }

    public LocalDate getDateEnded() {
        return this.dateEnded;
    }

    public Routine dateEnded(LocalDate dateEnded) {
        this.dateEnded = dateEnded;
        return this;
    }

    public void setDateEnded(LocalDate dateEnded) {
        this.dateEnded = dateEnded;
    }

    public LocalDate getGoalDate() {
        return this.goalDate;
    }

    public Routine goalDate(LocalDate goalDate) {
        this.goalDate = goalDate;
        return this;
    }

    public void setGoalDate(LocalDate goalDate) {
        this.goalDate = goalDate;
    }

    public Integer getStartingBodyWeight() {
        return this.startingBodyWeight;
    }

    public Routine startingBodyWeight(Integer startingBodyWeight) {
        this.startingBodyWeight = startingBodyWeight;
        return this;
    }

    public void setStartingBodyWeight(Integer startingBodyWeight) {
        this.startingBodyWeight = startingBodyWeight;
    }

    public Integer getEndingBodyWeight() {
        return this.endingBodyWeight;
    }

    public Routine endingBodyWeight(Integer endingBodyWeight) {
        this.endingBodyWeight = endingBodyWeight;
        return this;
    }

    public void setEndingBodyWeight(Integer endingBodyWeight) {
        this.endingBodyWeight = endingBodyWeight;
    }

    public Set<Excercise> getExcercises() {
        return this.excercises;
    }

    public Routine excercises(Set<Excercise> excercises) {
        this.setExcercises(excercises);
        return this;
    }

    public Routine addExcercise(Excercise excercise) {
        this.excercises.add(excercise);
        excercise.setRoutine(this);
        return this;
    }

    public Routine removeExcercise(Excercise excercise) {
        this.excercises.remove(excercise);
        excercise.setRoutine(null);
        return this;
    }

    public void setExcercises(Set<Excercise> excercises) {
        if (this.excercises != null) {
            this.excercises.forEach(i -> i.setRoutine(null));
        }
        if (excercises != null) {
            excercises.forEach(i -> i.setRoutine(this));
        }
        this.excercises = excercises;
    }

    public Set<ApplicationUser> getUsers() {
        return this.users;
    }

    public Routine users(Set<ApplicationUser> applicationUsers) {
        this.setUsers(applicationUsers);
        return this;
    }

    public Routine addUser(ApplicationUser applicationUser) {
        this.users.add(applicationUser);
        applicationUser.getRoutines().add(this);
        return this;
    }

    public Routine removeUser(ApplicationUser applicationUser) {
        this.users.remove(applicationUser);
        applicationUser.getRoutines().remove(this);
        return this;
    }

    public void setUsers(Set<ApplicationUser> applicationUsers) {
        if (this.users != null) {
            this.users.forEach(i -> i.removeRoutine(this));
        }
        if (applicationUsers != null) {
            applicationUsers.forEach(i -> i.addRoutine(this));
        }
        this.users = applicationUsers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Routine)) {
            return false;
        }
        return id != null && id.equals(((Routine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Routine{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dateStarted='" + getDateStarted() + "'" +
            ", dateEnded='" + getDateEnded() + "'" +
            ", goalDate='" + getGoalDate() + "'" +
            ", startingBodyWeight=" + getStartingBodyWeight() +
            ", endingBodyWeight=" + getEndingBodyWeight() +
            "}";
    }
}
