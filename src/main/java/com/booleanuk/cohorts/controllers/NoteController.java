package com.booleanuk.cohorts.controllers;

import com.booleanuk.cohorts.models.ERole;
import com.booleanuk.cohorts.models.Note;
import com.booleanuk.cohorts.models.User;
import com.booleanuk.cohorts.payload.response.*;
import com.booleanuk.cohorts.repository.NoteRepository;
import com.booleanuk.cohorts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class NoteController {
    @Autowired
    NoteRepository noteRepository;

    UserRepository userRepository;

    @GetMapping("users/{userId}/notes")
    public ResponseEntity<Response> getNotesByUserId(@PathVariable int id) {
        User student = this.userRepository.findById(id).orElse(null);
        if (student == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("student with that id was not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        var notes = noteRepository.findAllByUser(student);

        DataResponse<List<Note>> noteListResponse = new DataResponse<>();
        noteListResponse.set(notes);
        return ResponseEntity.ok(noteListResponse);
    }

    private record NoteRequest(String title, String text, LocalDate date, LocalTime time) {}

    @PostMapping("users/{userId}/notes")
    public ResponseEntity<Response> createNoteByUserId(@PathVariable int id, @RequestBody NoteRequest noteRequest) {
        User student = this.userRepository.findById(id).orElse(null);
        if (student == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("student with that id was not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        if (student.getRoles().isEmpty() || !student.getRoles().contains(ERole.ROLE_STUDENT)) {
            return ResponseEntity.status(400).body(new ErrorResponse("User was not a student"));
        }

        Note newNote = new Note();
        newNote.setUser(student);
        newNote.setTitle(noteRequest.title());
        newNote.setText(noteRequest.text());
        newNote.setDate(noteRequest.date());
        newNote.setTime(noteRequest.time());

        noteRepository.save(newNote);

        DataResponse<Note> noteResponse = new DataResponse<>();
        noteResponse.set(newNote);
        return new ResponseEntity<>(noteResponse, HttpStatus.CREATED);
    }

    @PutMapping("notes/{noteId}")
    public ResponseEntity<Response> updateNote(@PathVariable int noteId, @RequestBody NoteRequest noteRequest) {
        Note noteToUpdate = this.noteRepository.findById(noteId).orElse(null);
        if (noteToUpdate == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Note with that id was not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        noteToUpdate.setTitle(noteRequest.title());
        noteToUpdate.setText(noteRequest.text());
        noteToUpdate.setDate(noteRequest.date());
        noteToUpdate.setTime(noteRequest.time());

        noteRepository.save(noteToUpdate);

        DataResponse<Note> noteResponse = new DataResponse<>();
        noteResponse.set(noteToUpdate);
        return ResponseEntity.ok(noteResponse);
    }

    @DeleteMapping("notes/{noteId}")
    public ResponseEntity<Response> deleteNote(@PathVariable int noteId) {
        Note noteToDelete = this.noteRepository.findById(noteId).orElse(null);
        if (noteToDelete == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("Note with that id was not found"));
        }
        noteRepository.delete(noteToDelete);

        DataResponse<Note> noteResponse = new DataResponse<>();
        noteResponse.set(noteToDelete);
        return ResponseEntity.ok(noteResponse);
    }
}
