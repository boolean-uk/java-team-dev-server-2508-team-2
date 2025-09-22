package com.booleanuk.cohorts.repository;

import com.booleanuk.cohorts.models.Note;
import com.booleanuk.cohorts.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    List<Note> findAllByUser(User user);
}
