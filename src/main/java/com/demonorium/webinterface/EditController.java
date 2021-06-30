package com.demonorium.webinterface;

import com.demonorium.database.StorageController;
import com.demonorium.database.entity.Group;
import com.demonorium.database.entity.Note;
import com.demonorium.database.entity.User;
import com.demonorium.webinterface.view.NoteView;
import com.demonorium.webinterface.view.SimpleViewAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

@Controller
public class EditController {
    @Autowired
    StorageController storage;

    @PostMapping("/request/save/{noteId}")
    public String edit(
            @PathVariable Long noteId,
            @ModelAttribute("note") NoteView form,
                       Principal principal, Model model) {
        Optional<Note> selected = storage.note.findById(noteId);

        if (!selected.isPresent())
            return "redirect:/home";

        Note note = selected.get();
        Group group = note.getGroup();

        if (group == null)
            return "redirect:/home";

        User user = storage.user.getByUsername(principal.getName());
        if (group.getUser() == user) {
            note.setName(form.getName());
            note.setContent(form.getContent());
            note.setUpdateDate(new Date());
            storage.note.save(note);
        }

        return "redirect:/home/" + group.getId() + "/" + note.getId();
    }

    @PostMapping("/request/new_note/{selectedGroup}")
    public String createNote(@PathVariable Long selectedGroup, Principal principal, Model model) {

        User user = storage.user.getByUsername(principal.getName());
        if (selectedGroup == null) {
            return "redirect:/home";
        }
        Group group = storage.group.getById(selectedGroup);
        if ((group != null) && (group.getUser() == user)) {
            Note note = new Note("", "", group);
            storage.note.save(note);
            return "redirect:/home/"+selectedGroup+"/"+note.getId();
        }
        return "redirect:/home";
    }

    @PostMapping("/request/new_group")
    public String createGroup(Principal principal, Model model) {
        User user = storage.user.getByUsername(principal.getName());

        return "redirect:/home";
    }
}
