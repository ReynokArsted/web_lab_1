package com.example.lab1.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="note")
public class Note {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteId;
    
    private String title;
    private String content;
    private LocalDateTime last_edit_time;

    @ManyToMany
    @JoinTable(
        name = "note_tags",
        joinColumns = @JoinColumn(name = "note_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "attachment_id") 
    private Attachment attachment;        

    protected Note() {}

    //public Note(String title, String content, List<Tag> tags, Attachment attachment) {
    public Note(String title, String content, List<Tag> tags) {
        this.title = title;
        this.content = content;
        this.tags = tags;
    }

    public Long getNote_id() {return noteId;}
    public String getTitle() {return title;}
    public String getContent() {return content;}
    public LocalDateTime getLastEditTime() {return last_edit_time;}
    public List<Tag> getNote_tags() {return tags;}
    public Attachment getAttachment() {return attachment;}

    public void setTitle(String title) {this.title = title;}
    public void setContent(String content) {this.content = content;}
    public void setLastEditTime(LocalDateTime time) {this.last_edit_time = time;}
    public void setTags(List<Tag> tags) {this.tags = tags;}
    public void setAttachment(Attachment atch) {this.attachment = atch;}
}