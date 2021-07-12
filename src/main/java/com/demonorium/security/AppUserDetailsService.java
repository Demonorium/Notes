package com.demonorium.security;

import com.demonorium.utils.GroupFlags;
import com.demonorium.database.StorageController;
import com.demonorium.database.entity.Group;
import com.demonorium.database.entity.Note;
import com.demonorium.database.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class AppUserDetailsService implements UserDetailsService {
    @Autowired
    private StorageController storage;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    MessageSource messageSource;

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
            Group group = new Group(messageSource.getMessage("hello_group",null, Locale.getDefault()), user);
            storage.group.save(group);
            Group available = new Group(messageSource.getMessage("for_me", null, Locale.getDefault()), user);
            available.onFlag(
                    GroupFlags.DEFAULT.flag()
                    | GroupFlags.NO_RENAME.flag()
                    | GroupFlags.FIXED_LEVEL_ROOT.flag()
                    | GroupFlags.NO_ADD.flag());
            available.setNetGroup(true);

            storage.group.save(available);

            storage.note.save(new Note(
                    messageSource.getMessage("hello_head",null, Locale.getDefault()),
                    messageSource.getMessage("hello_msg",null, Locale.getDefault()),
                    group));
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
