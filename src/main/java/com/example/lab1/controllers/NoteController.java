package com.example.lab1.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{id}") // Get note by ID
    public Note getNoteById(@PathVariable long id) {
        return noteRepository.findById(id).orElseThrow(() 
            -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
    }

    @GetMapping("/title/{title}") // Get notes by title
    public List<Note> getNoteByTitle(@PathVariable String title) {
        List<Note> notes_list = noteRepository.findAllByTitle(title);
        if (notes_list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found");
        }
        return notes_list;
    }

    @GetMapping // Get all notes
    public List<Note> getAllNotes() {
        return noteRepository.findAll(Sort.by("noteId").descending());
    }

    @PostMapping("search") // Search notes by substring
    public List<Note> searchNotes(@RequestBody SearchDTO searchDTO) {
        String substring = searchDTO.substring.toLowerCase();
        List<Note> allNotes = noteRepository.findAll();
        List<Note> result = new ArrayList<>();

        for (Note note : allNotes) {
            if (note.getTitle().toLowerCase().contains(substring) || 
                note.getContent().toLowerCase().contains(substring)) {
                result.add(note);
            }
        }

        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No matching notes found");
        }

        return result;
    }
    
    @PostMapping("/{id}") // Edit note
    public Note edit_note(@PathVariable long id, @RequestBody NoteDTO noteDTO) {
        Optional<Note> noteOptional = noteRepository.findById(id);
        Note note = noteOptional.orElseThrow(() -> 
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));

        note.setTitle(noteDTO.title);
        note.setContent(noteDTO.content);
        note.setTags(new ArrayList<>(
            noteDTO.tagTitles.stream()
                .map(title -> tagRepository.findByTitle(title)
                .orElseGet(() -> tagRepository.save(new Tag(title))))
            .toList()
        ));
        return noteRepository.save(note);
    }


    @PostMapping // Create note
    public Note createNote(@RequestBody NoteDTO noteDTO) {
        List<Tag> tags = null;

        if (noteDTO.tagTitles != null && !noteDTO.tagTitles.isEmpty()) {
            tags = noteDTO.tagTitles.stream()
                .map(title -> tagRepository.findByTitle(title)
                    .orElseGet(() -> tagRepository.save(new Tag(title))))
                .toList();
        }

        Note note = new Note(noteDTO.title, noteDTO.content, tags);
        //Note note = new Note(noteDTO.title, noteDTO.content, tags, noteDTO.attachment);
        return noteRepository.save(note);
    }

    @DeleteMapping("/{id}") // Delete note
    public void deleteNote(@PathVariable long id) {
        Note note = noteRepository.findById(id).orElseThrow(() -> 
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
        noteRepository.delete(note);
    }
}