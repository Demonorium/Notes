package com.demonorium.database.repository;

import com.demonorium.database.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessRepository extends JpaRepository<Access, String>  {
    List<Access> getByUser(User user);
    List<Access> getByAccessReference(NoteAccessReference accessReference);
    Access getByUserAndAccessReference(User user, NoteAccessReference accessReference);
    List<Access> getByUserAndAccessReference_NoteIs(User user, Note note);
}
