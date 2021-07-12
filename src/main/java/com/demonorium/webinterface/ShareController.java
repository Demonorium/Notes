package com.demonorium.webinterface;

import com.demonorium.database.StorageController;
import com.demonorium.database.entity.*;
import com.demonorium.webinterface.view.NoteView;
import com.demonorium.webinterface.view.ShareNoteView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class ShareController {

    @Autowired
    StorageController storage;

    @GetMapping("/share/**")
    String share(HttpServletRequest request, Principal principal, Model model) {
        String full = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String shr = full.substring(full.indexOf("/share/") + "/share/".length());

        Optional<NoteAccessReference> reference = storage.refs.findById(shr);

        if (!reference.isPresent())
            return "notfound";

        Note note = reference.get().getNote();
        if (principal == null) {
            model.addAttribute("note", new ShareNoteView(note.getName(), note.getContent()));
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
