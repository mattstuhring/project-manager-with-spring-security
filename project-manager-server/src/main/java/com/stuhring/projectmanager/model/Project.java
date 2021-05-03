package com.stuhring.projectmanager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Entity:
 *  - Class which should be persisted in a database.
 *  - JPA uses a database table for every entity.
 *  - Persisted instances of the class will be represented as one row in the table.
 *  - JPA allows to auto-generate the primary key in the database via the @GeneratedValue annotation.
 *  - By default, the table name corresponds to the class name.
 *  - H2 Database UI: localhost:8080/h2-console
 */
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Project name is required")
    private String projectName;
    @NotBlank(message = "Project Identifier is required")
    @Size(min = 4, max = 5, message = "Please use 4 to 5 characters")
    @Column(updatable = false, unique = true) // Constraint happens after validation, and at the db persist layer.
    private String projectIdentifier; // Do no change naming convention to "projectId", issues with JPA using common naming.
    @NotBlank(message = "Project description is required")
    private String description;
    @JsonFormat(pattern = "YYYY-MM-dd")
    private Date start_date;
    @JsonFormat(pattern = "YYYY-MM-dd")
    private Date end_date;
    @JsonFormat(pattern = "YYYY-MM-dd")
    @Column(updatable = false)
    private Date created_At;
    @JsonFormat(pattern = "YYYY-MM-dd")
    private Date updated_At;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "project")
    @JsonIgnore // Decouple Backlog from Project
    private Backlog backlog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    private String projectLeader;

    // The JPA specification requires that all persistent classes have a no-arg constructor.
    public Project() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectIdentifier() {
        return projectIdentifier;
    }

    public void setProjectIdentifier(String projectIdentifier) {
        this.projectIdentifier = projectIdentifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Date getCreated_At() {
        return created_At;
    }

    public void setCreated_At(Date created_At) {
        this.created_At = created_At;
    }

    public Date getUpdated_At() {
        return updated_At;
    }

    public void setUpdated_At(Date updated_At) {
        this.updated_At = updated_At;
    }

    public Backlog getBacklog() {
        return backlog;
    }

    public void setBacklog(Backlog backlog) {
        this.backlog = backlog;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getProjectLeader() {
        return projectLeader;
    }

    public void setProjectLeader(String projectLeader) {
        this.projectLeader = projectLeader;
    }

    // Everytime a new object is created it will store the date
    // Before persist is called for a new entity
    @PrePersist
    protected void onCreate() {
        this.created_At = new Date();
    }

    // Everytime an object is updated it will store the new date
    // Before the update operation
    @PreUpdate
    protected void onUpdate() {
        this.updated_At = new Date();
    }
}
