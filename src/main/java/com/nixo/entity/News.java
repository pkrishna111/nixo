package com.nixo.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "news")
public class News implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // DB column is `id`
    private int id;

    // Map to the DB column sharer_id (created by migration above)
    @ManyToOne
    @JoinColumn(name = "sharer_id", referencedColumnName = "user_id", nullable = true)
    private User sharer;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    // status e.g. "pending", "approved", "rejected"
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // persistent likes count (keeps denormalized total for speed)
    private int likes = 0;

    // transient to allow additional display calculations
    @Transient
    private long likeCount;

    public News() {
        this.createdAt = LocalDateTime.now();
        this.status = "pending";
    }

    public News(User sharer, String title, String content) {
        this.sharer = sharer;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.status = "pending";
    }

    // ===== Getters / Setters =====

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public User getSharer() {
        return sharer;
    }

    public void setSharer(User sharer) {
        this.sharer = sharer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }
}
