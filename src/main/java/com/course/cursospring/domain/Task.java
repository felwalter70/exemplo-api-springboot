package com.course.cursospring.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Objects;

@Entity 
@Table (name = Task.TABLE_NAME)
public class Task {
    public static final String TABLE_NAME = "tasks";


    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id", unique = true)
    private Long id;


    @ManyToOne 
    @JoinColumn (name = "user_id", nullable = false, updatable = false)
    private User user;


    @Column (name = "description", length = 255, nullable = false)
    @NotBlank 
    @Size (min = 1, max = 255)
    private String description;


    public Task() {
    }

    public Task(Long id, User user, String description) {
        this.id = id;
        this.user = user;
        this.description = description;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Task id(Long id) {
        setId(id);
        return this;
    }

    public Task user(User user) {
        setUser(user);
        return this;
    }

    public Task description(String description) {
        setDescription(description);
        return this;
    }

    @Override 
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (o == null && !(o instanceof Task))
            return false;

        Task other = (Task) o;

        return Objects.equals(this.id, other.id) && Objects.equals(this.user, other.user) && Objects.equals(this.description, other.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, description);
    }
}
