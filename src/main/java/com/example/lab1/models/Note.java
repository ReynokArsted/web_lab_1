package com.example.lab1.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "note_id",     
    "title",        
    "content",
    "createdTime", 
    "lastEditTime",
    "note_tags",   
    "attachment"
})
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="note")
public class Note {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteId;
    
    private String title;
    private String content;

    @CreatedDate
    @Column(updatable = false) 
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime lastEditTime;

    @ManyToMany
    @JoinTable(
        name = "note_tags",
        joinColumns = @JoinColumn(name = "note_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "attachment_id")
    private Attachment attachment;        

    protected Note() {}

    public Note(String title, String content, List<Tag> tags, Attachment attachment) {
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.attachment = attachment;
    }

    public Long getNote_id() {return noteId;}
    public String getTitle() {return title;}
    public String getContent() {return content;}
    public List<Tag> getNote_tags() {return tags;}
    public Attachment getAttachment() {return attachment;}
    public LocalDateTime getCreatedTime() {return createdTime;}
    public LocalDateTime getLastEditTime() {return lastEditTime;}

    public void setTitle(String title) {this.title = title;}
    public void setContent(String content) {this.content = content;}
    public void setTags(List<Tag> tags) {this.tags = tags;}
    public void setAttachment(Attachment atch) {this.attachment = atch;}
}