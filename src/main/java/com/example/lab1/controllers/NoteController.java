package com.example.lab1.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.lab1.models.Attachment;
import com.example.lab1.models.Note;
import com.example.lab1.models.Tag;
import com.example.lab1.repositories.AttachRepository;
import com.example.lab1.repositories.NoteRepository;
import com.example.lab1.repositories.TagCleanerService;
import com.example.lab1.repositories.TagRepository;


@RestController
@RequestMapping("/notes")
public class NoteController {
    private final AttachRepository attachRepository;
    private final NoteRepository noteRepository;
    private final TagRepository tagRepository;
    private final TagCleanerService tagCleaner;

    public NoteController(
        NoteRepository noteRepository, 
        TagRepository tagRepository, 
        AttachRepository attachRepository,
        TagCleanerService tagCleaner) 
    {
        this.noteRepository = noteRepository;
        this.tagRepository = tagRepository;
        this.attachRepository = attachRepository;
        this.tagCleaner = tagCleaner;
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
        if (searchDTO == null || searchDTO.substring.isBlank())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Substring is empty");
        }
        String substring = searchDTO.substring.toLowerCase();
        List<Note> allNotes = noteRepository.findAll();
        List<Note> result = new ArrayList<>();

        for (Note note : allNotes) {
            if (note.getTitle() == null && note.getContent() != null)
            {
                if (note.getContent().toLowerCase().contains(substring)) {
                    result.add(note);
                }
            }
            else if (note.getTitle() != null && note.getContent() == null)
            {
                if (note.getTitle().toLowerCase().contains(substring)) {
                    result.add(note);
                }
            }
            else if (note.getTitle() != null && note.getContent() != null)
            {
                if (note.getTitle().toLowerCase().contains(substring) || 
                    note.getContent().toLowerCase().contains(substring)) {
                    result.add(note);
                }
            }
        }

        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No matching notes found");
        }

        return result;
    }
    
    @PostMapping("/{id}") // Edit note
    public Note edit_note(
        @PathVariable long id,
        @RequestPart(value = "note", required = false) NoteDTO noteDTO,
        @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {
        Optional<Note> noteOptional = noteRepository.findById(id);
        Note note = noteOptional.orElseThrow(() -> 
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));

        if (noteDTO != null) {
            if (noteDTO.title != null) {
                note.setTitle(noteDTO.title);
            }
            if (noteDTO.content != null) {
                note.setContent(noteDTO.content);
            }
            if (noteDTO.tagTitles != null && !noteDTO.tagTitles.isEmpty()) {
            note.setTags(new ArrayList<>(
                noteDTO.tagTitles.stream()
                    .map(title -> tagRepository.findByTitle(title)
                        .orElseGet(() -> tagRepository.save(new Tag(title))))
                    .toList()
                ));
            }
        }

        if (file != null && !file.isEmpty()) {
            note.setAttachment(new Attachment(file.getOriginalFilename(), file.getBytes()));
        }

        noteRepository.save(note);
        tagCleaner.cleanupOrphanTags(); // Deleting unused tags
        return note;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // Create note
    public ResponseEntity<Note> createNote(
            @RequestPart("note") NoteDTO noteDTO,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {
        List<Tag> tags = null;
        if (noteDTO.tagTitles != null && !noteDTO.tagTitles.isEmpty()) {
            tags = noteDTO.tagTitles.stream()
                .map(title -> tagRepository.findByTitle(title)
                    .orElseGet(() -> tagRepository.save(new Tag(title))))
                .toList();
        }

        Attachment attachment = null;
        if (file != null && !file.isEmpty()) {
            attachment = new Attachment(file.getOriginalFilename(), file.getBytes());
        }

        Note savedNote = noteRepository.save(
            new Note(noteDTO.title, noteDTO.content, tags, attachment));
        return ResponseEntity.ok(savedNote);
    }

    @DeleteMapping("/{id}/attachment") // Delete attachment by note id
    public void deleteAttachment(@PathVariable long id) {
        Note note = noteRepository.findById(id).orElseThrow(() -> 
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
        
        Attachment atch = note.getAttachment();
        if (atch != null)
        {
            Attachment rep_atch = attachRepository.findById(atch.getAttachment_id())
                .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Attachment not found"));
            note.setAttachment(null);
            attachRepository.delete(rep_atch);
        }
        noteRepository.save(note);
    }

    @DeleteMapping("/{id}") // Delete note
    public void deleteNote(@PathVariable long id) {
        Note note = noteRepository.findById(id).orElseThrow(() -> 
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
        Attachment atch = note.getAttachment();
        if (atch != null)
        {
            Attachment rep_atch = attachRepository.findById(atch.getAttachment_id())
                .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Attachment not found"));
            note.setAttachment(null);
            attachRepository.delete(rep_atch);
        }
        noteRepository.delete(note);
        tagCleaner.cleanupOrphanTags(); // Deleting unused tags
    }

    @GetMapping("/{id}/attachment") // Get note's attachment
    public ResponseEntity<byte[]> downloadAttachment(@PathVariable Long id) {
        Note note = noteRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));

        Attachment attachment = note.getAttachment();
        if (attachment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attachment not found");
        }

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getName() + "\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(attachment.getData());
    }
}