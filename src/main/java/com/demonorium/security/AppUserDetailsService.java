package com.demonorium.security;

import com.demonorium.database.StorageController;
import com.demonorium.database.entity.Group;
import com.demonorium.database.entity.Note;
import com.demonorium.database.entity.User;
import com.demonorium.database.repository.UserRepository;
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
            Group group = new Group("default", user);
            storage.group.save(group);
            Group avvv = new Group("Доступные мне", user);
            storage.group.save(avvv);

            storage.note.save(new Note(
                    "Добро пожаловать",
                    "Это заметка, вы можете её редактировать. Нажмите на +, " +
                            "чтобы создать новую заметку или группу заметок.", group));
            return true;
        }
        return false;
    }
}
