package com.demonorium.database;

import com.demonorium.database.repository.GroupRepository;
import com.demonorium.database.repository.NoteRepository;
import com.demonorium.database.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StorageController {
    @Autowired
    public GroupRepository group;
    @Autowired
    public NoteRepository note;
    @Autowired
    public UserRepository user;


}
