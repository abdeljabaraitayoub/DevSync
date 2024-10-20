package com.devsync.domain.entities;

import com.devsync.domain.enums.TaskStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "tasks")

public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;

    @Column(name = "date_created")
    private LocalDate dateCreated;

    @Column(name = "date_end")
    private LocalDate dateEnd;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;


    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User createdByUser;

    @Column(name = "is_requested")
    private boolean isRequested = false;


    private LocalDateTime dateRequested = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "task_tags",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = {
                    @JoinColumn(name = "tag_id"),
            })

    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Tag> tags;

    public Task() {
    }


    public Task(Long id, String title, String description, TaskStatus status, LocalDate dateCreated, LocalDate dateEnd, User user, User createdByUser) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.dateCreated = dateCreated;
        this.dateEnd = dateEnd;
        this.user = user;
        this.createdByUser = createdByUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }


    public boolean isRequested() {
        return isRequested;
    }

    public void setRequested(boolean requested) {
        isRequested = requested;
    }

    public LocalDateTime getDateRequested() {
        return dateRequested;
    }

    public void setDateRequested(LocalDateTime dateRequested) {
        this.dateRequested = dateRequested;
    }
}
