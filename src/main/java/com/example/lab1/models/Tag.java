package com.example.lab1.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tag")
public class Tag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tag_id;
    private String title;

    protected Tag() {}

    public Tag(String title) {
        this.title = title;
    }
    
    public String getTagTitle() {
        return title;
    }

    @Override
    public String toString() {
        return String.format("Tag[tag_id=%d, title='%s']", tag_id, title);
    }
}
