package com.demonorium.database.repository;

import com.demonorium.database.entity.Group;
import com.demonorium.database.entity.Note;
import com.demonorium.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;


import java.util.*;

public interface NoteRepository extends CrudRepository<Note, Long> {
    Note getNoteById(Long id);
    List<Note> getAllByGroup(Group group);

}
