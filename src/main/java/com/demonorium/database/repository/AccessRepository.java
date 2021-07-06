package com.demonorium.database.repository;

import com.demonorium.database.entity.Access;
import com.demonorium.database.entity.Group;
import com.demonorium.database.entity.Note;
import com.demonorium.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessRepository extends JpaRepository<Access, String>  {
    List<Access> getByUser(User user);
    List<Access> getByNote(Note note);

}
