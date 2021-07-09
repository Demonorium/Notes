package com.demonorium.webinterface;

import com.demonorium.database.StorageController;
import com.demonorium.database.entity.*;
import com.demonorium.webinterface.view.NoteView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class ShareController {

    @Autowired
    StorageController storage;

    @GetMapping("/share/{code}")
    String share(@PathVariable("code") String shr, Principal principal, Model model) {
        Optional<NoteAccessReference> reference = storage.refs.findById(shr);

        if (!reference.isPresent())
            return "notfound";

        Note note = reference.get().getNote();
        if (principal == null) {
            model.addAttribute("note", new NoteView(note.getName(), note.getContent()));
            return "share-promo";
        } else {
            User user = storage.user.getByUsername(principal.getName());
            if (user == note.getGroup().getUser())
                return "redirect:/home/" + note.getGroup().getId() + "/" + note.getId();

            Access access = storage.access.getByUserAndAccessReference(user, reference.get());

            if (access == null) {
                access = new Access(user, reference.get());
                storage.access.save(access);
            }
            Group group = storage.group.getByUserAndNetGroupIs(user, true);
            return "redirect:/home/" + group.getId() + "/" + note.getId();
        }
    }

}
