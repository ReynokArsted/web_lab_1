package com.example.lab1.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lab1.models.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByTitle(String title);
}