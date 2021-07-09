package com.demonorium.database.repository;

import com.demonorium.database.entity.NoteAccessReference;
import com.demonorium.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReferenceRepository extends JpaRepository<NoteAccessReference, String> {
    NoteAccessReference getByUser(User user);
}
