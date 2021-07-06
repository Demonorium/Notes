package com.demonorium.security;

import com.demonorium.utils.GroupFlags;
import com.demonorium.database.StorageController;
import com.demonorium.database.entity.Group;
import com.demonorium.database.entity.Note;
import com.demonorium.database.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AppUserDetailsService implements UserDetailsService {
    @Autowired
    private StorageController storage;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDetails loadUserByUsername(String username) {
        User user = storage.user.getByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("Unknown username: " + username);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("user")
                .build();
    }


    public boolean newUser(String username, String email, String password) {
        User user = storage.user.getByUsername(username);
        if (user == null) {
            user = new User(username, email, bCryptPasswordEncoder.encode(password));
            storage.user.save(user);
            Group group = new Group("Группа заметок", user);
            storage.group.save(group);
            Group available = new Group("Доступные мне", user);
            available.onFlag(
                    GroupFlags.DEFAULT.flag()
                    | GroupFlags.NO_RENAME.flag()
                    | GroupFlags.FIXED_LEVEL_ROOT.flag()
                    | GroupFlags.NO_ADD.flag());
            available.setNetGroup(true);

            storage.group.save(available);

            storage.note.save(new Note(
                    "Добро пожаловать",
                    "Это заметка, вы можете её редактировать. Нажмите на +, " +
                            "чтобы создать новую заметку или группу заметок.", group));
            return true;
        }
        return false;
    }

    public boolean removeUser(String password, String username) {
        User user = storage.user.getByUsername(username);
        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            storage.user.delete(user);
            return true;
        }
        return false;
    }

    public boolean changePassword(String password, String newPassword, String username) {
        User user = storage.user.getByUsername(username);
        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            storage.user.save(user);
            return true;
        }
        return false;
    }
}
