package com.demonorium.webinterface;

import com.demonorium.database.StorageController;
import com.demonorium.database.entity.Group;
import com.demonorium.database.entity.Note;
import com.demonorium.database.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Controller
public class MainPageController {

    @Autowired
    StorageController storage;


    @GetMapping("/home")
    String home(Principal principal, Model model) {
        User user = storage.user.getByUsername(principal.getName());
        model.addAttribute("user", user);
        List<Group> groups = storage.group.getByUser(user);
        model.addAttribute("groups", groups);
        model.addAttribute("selectedGroup", groups.get(0).getId());
        Collection<Note> notes = groups.get(0).getNotes();
        model.addAttribute("notes", notes);
        model.addAttribute("noteIsSelected", false);
        model.addAttribute("selectedNote", -1);
        return "home";
    }
}
