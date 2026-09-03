package com.anutej.openstream_api.entity;

import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(name = "follows", uniqueConstraints = @UniqueConstraint(columnNames = { "follower_id", "followed_id" }))
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_id", nullable = false)
    private User followed;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollowed(User followed) {
        this.followed = followed;
    }

    public User getFollowed() {
        return followed;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    // Constructor

    public Follow() {

    }

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }

}
