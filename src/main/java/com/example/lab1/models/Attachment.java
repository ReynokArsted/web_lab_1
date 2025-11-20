package com.example.lab1.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name="attachment")
public class Attachment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id")
    private Long attachment_id;
    private String name;

    private byte[] data;

    protected Attachment() {}

    public Attachment(String name, byte[] data) {
        this.name = name;
        this.data = data;
    }

    public Long getAttachment_id() {return attachment_id;}
    public String getName() {return name;}
    public byte[] getData() {return data;}

    public void setName(String name) {this.name = name;}
    public void setData(byte[] data) {this.data = data;}
}