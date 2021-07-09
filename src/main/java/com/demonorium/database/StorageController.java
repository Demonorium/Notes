package com.demonorium.database;

import com.demonorium.database.entity.Access;
import com.demonorium.database.entity.Group;
import com.demonorium.database.entity.Note;
import com.demonorium.database.entity.User;
import com.demonorium.database.repository.*;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
public class StorageController {
    public MessageDigest md5Digest;


    public StorageController() {
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public synchronized String md5(String input) {
        return Base64.encode(md5Digest.digest(input.getBytes(StandardCharsets.UTF_8)));
    }


    @Autowired
    public GroupRepository group;
    @Autowired
    public NoteRepository note;
    @Autowired
    public UserRepository user;
    @Autowired
    public AccessRepository access;
    @Autowired
    public ReferenceRepository refs;


    private static String rndInsert(String str, String ... ins) {
        Random random = new Random(new Date().getTime());
        for (String in : ins) {
            int pos = ((int) random.nextLong()) % str.length();

            str = str.substring(0, pos) + in + str.substring(pos);
        }

        return str;
    }

    public String generateAccessToken(Note note, int rights) {
        Group group = note.getGroup();
        User user = group.getUser();
        Date date = new Date();
        date.setTime(date.getTime() + (long) rights);
        String first = Long.toHexString(note.getId());
        String second = Long.toHexString(group.getId());

        String user_hash = md5(user.getUsername()+user.getEmail() + date.getTime() * (long) rights);
        String note_hash = md5(note.getName() + note.getContent() + Long.toString(date.getTime(), 7));

        return rndInsert(user_hash + note_hash, first, second, Long.toHexString(date.getTime()* (long) rights)) + first;
    }

    public void removeNote(Note nt) {
        note.delete(nt);
    }
    public void updateNote(Note nt) {
        nt.setUpdateDate(new Date());
        note.save(nt);
    }

}
