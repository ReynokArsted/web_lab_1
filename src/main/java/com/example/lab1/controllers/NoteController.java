package com.example.lab1.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

import com.example.lab1.models.Note;
import com.example.lab1.models.Tag;
import com.example.lab1.repositories.NoteRepository;
import com.example.lab1.repositories.TagRepository;


@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteRepository noteRepository;
    private final TagRepository tagRepository;

    public NoteController(NoteRepository noteRepository, TagRepository tagRepository) {
        this.noteRepository = noteRepository;
        this.tagRepository = tagRepository;
    }

    @GetMapping("/{id}")
    public Note getNoteById(@PathVariable Long id) {
        return noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Note not found"));
    }

    @GetMapping
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @PostMapping
    public Note createNote(@RequestBody NoteDTO noteDTO) {
        Tag tag = tagRepository.findByTitle(noteDTO.tagTitle)
                .orElseGet(() -> {
                    Tag newTag = new Tag(noteDTO.tagTitle);
                    return tagRepository.save(newTag);
                });

        Note note = new Note(noteDTO.title, noteDTO.content, tag);
        return noteRepository.save(note);
    }
}