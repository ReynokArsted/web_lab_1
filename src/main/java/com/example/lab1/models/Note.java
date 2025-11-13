package com.example.lab1.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name="note")
public class Note {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long note_id;
    
    @ManyToMany
    @JoinTable(
        name = "note_tags",
        joinColumns = @JoinColumn(name = "note_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;


    private String title;
    private String content;

    protected Note() {}

    public Note(String title, String content, List<Tag> tags) {
        this.title = title;
        this.content = content;
        this.tags = tags;
    }

    public void setNoteTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Long getNote_id() { return note_id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public List<Tag> getNote_tags() { return tags; }

    @Override
    public String toString() {
        return "Note{" + "noteId=" + note_id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", tags=" + tags +
                '}';
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