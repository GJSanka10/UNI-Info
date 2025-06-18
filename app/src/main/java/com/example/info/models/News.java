package com.example.info.models;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.Date;

public class News implements Serializable {
    private String id;
    private String title;
    private String content;
    private String description;
    private String category;
    private String imageUrl;
    private Timestamp publishedAt;
    private boolean isBreaking;
    private boolean isTrending;
    private String author;
    private int viewCount;

    // Default constructor required for Firestore
    public News() {}
    // Constructor with essential fields
    public News(String title, String content, String category, String imageUrl) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.imageUrl = imageUrl;
        this.publishedAt = Timestamp.now();
        this.isBreaking = false;
        this.isTrending = false;
        this.viewCount = 0;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title != null ? title : "";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content != null ? content : "";
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category != null ? category : "Academic";
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl != null ? imageUrl : "";
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Timestamp getPublishedAt() {
        return publishedAt != null ? publishedAt : Timestamp.now();
    }

    public void setPublishedAt(Timestamp publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Date getPublishedDate() {
        return publishedAt != null ? publishedAt.toDate() : new Date();
    }

    public boolean isBreaking() {
        return isBreaking;
    }

    public void setBreaking(boolean breaking) {
        isBreaking = breaking;
    }

    public boolean isTrending() {
        return isTrending;
    }

    public void setTrending(boolean trending) {
        isTrending = trending;
    }

    public String getAuthor() {
        return author != null ? author : "Unknown";
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    // Helper method to get formatted published time
    public String getFormattedTime() {
        if (publishedAt == null) return "Unknown time";

        Date now = new Date();
        Date published = publishedAt.toDate();
        long diffInMillis = now.getTime() - published.getTime();

        long diffInMinutes = diffInMillis / (60 * 1000);
        long diffInHours = diffInMillis / (60 * 60 * 1000);
        long diffInDays = diffInMillis / (24 * 60 * 60 * 1000);

        if (diffInMinutes < 60) {
            return diffInMinutes + " minutes ago";
        } else if (diffInHours < 24) {
            return diffInHours + " hours ago";
        } else {
            return diffInDays + " days ago";
        }
    }

    @Override
    public String toString() {
        return "News{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", publishedAt=" + publishedAt +
                '}';
    }
}