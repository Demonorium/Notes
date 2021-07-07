package com.demonorium.webinterface;

import com.demonorium.database.StorageController;
import com.demonorium.database.entity.Access;
import com.demonorium.database.entity.Group;
import com.demonorium.database.entity.Note;
import com.demonorium.database.entity.User;
import com.demonorium.webinterface.view.NoteView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
public class ShareController {

    @Autowired
    StorageController storage;

    @GetMapping("/share/{code}")
    String share(@PathVariable("code") String shr, Principal principal, Model model) {
        Note note = storage.note.getBySharecode(shr);
        if (note == null)
            return "notfound";

        if (principal == null) {
            model.addAttribute("note", new NoteView());
            return "share-promo";
        } else {
            User user = storage.user.getByUsername(principal.getName());
            Access access = storage.access.getByUserAndNote(user, note);
            if (access == null) {
                access = new Access(user, note);
                storage.access.save(access);
            }
            Group group = storage.group.getByUserAndNetGroupIs(user, true);
            return "redirect:/home/" + group.getId() + "/" + note.getId();
        }
    }

}
