package com.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Wish {
    @Id
    @GeneratedValue
    private Long id;
    private String content;          // 愿望内容
    private String blessing;         // 月神祝福
    private Instant createdAt = Instant.now();
    // 构造器、getter/setter 略
    // JPA 要求

    public Wish(String content, String blessing) {
        this.content = content;
        this.blessing = blessing;
    }

    // Getters
    public Long getId() { return id; }
    public String getContent() { return content; }
    public String getBlessing() { return blessing; }
    public Instant getCreatedAt() { return createdAt; }
}