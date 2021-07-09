package com.demonorium.database.repository;

import com.demonorium.database.entity.Note;
import com.demonorium.database.entity.NoteAccessReference;
import com.demonorium.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReferenceRepository extends JpaRepository<NoteAccessReference, String> {
    NoteAccessReference getByUserAndNote(User user, Note note);
    List<NoteAccessReference> getByNote(Note note);
}
