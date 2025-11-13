package com.example.lab1.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="note")
public class Note {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long note_id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tag_id") 
    private Tag note_tag; // FK

    private String title;
    private String content;

    protected Note() {}

    public Note(String title, String content, Tag note_tag) {
        this.title = title;
        this.content = content;
        this.note_tag = note_tag;
    }

    public void setNoteTag(Tag tag) {
        this.note_tag = tag;
    }

    public Long getNote_id() { return note_id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public Tag getNote_tag() { return note_tag; }

    @Override
    public String toString() {
        return String.format("Note[note_id=%d, tag ='%s' title='%s', content='%s']", 
        note_id, note_tag.getTagTitle() ,title, content);
    }
}


/*
@Entity
@Table(name="Task")
public class Task {
    private int task_id;
    private String name;
    private String description;
    private int start_time;
    private int end_time;
    private int start_date;
    private int end_time_date;
    private boolean is_done;
    private boolean is_impotant;
    private int user_id;
}
*/