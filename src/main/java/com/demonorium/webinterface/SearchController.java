package com.demonorium.webinterface;

import com.demonorium.database.StorageController;
import com.demonorium.database.entity.Access;
import com.demonorium.database.entity.Group;
import com.demonorium.database.entity.Note;
import com.demonorium.database.entity.User;
import com.demonorium.utils.AccessRights;
import com.demonorium.webinterface.view.NoteView;
import com.demonorium.webinterface.view.SearchView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.*;

@Controller
public class SearchController {
    @Autowired
    protected StorageController storage;


    @GetMapping("/search")
    String search(@ModelAttribute("search")   SearchView search,
                  Principal principal, Model model) {
        User user = storage.user.getByUsername(principal.getName());
        model.addAttribute("user", user);


        if ((search.getInput() != null) && (search.getTarget() != null)) {
            model.addAttribute("search", new SearchView(search.getInput(), search.getTarget()));

            switch (search.getTarget()) {
                case 1:
                    List<Group> groups = storage.group.findByUserAndNameLikeOrderByName(user, "%"+search.getInput()+"%");
                    model.addAttribute("list", groups);
                    model.addAttribute("isNotes", false);
                    break;
                case 2:
                    List<Note> notes = storage.note.findAllByGroupInAndNameLikeOrderByName(storage.group.findByUser(user), "%"+search.getInput()+"%");
                    List<Access> accesses = storage.access.getByUserAndAccessReference_NoteNameLike(user, "%"+search.getInput()+"%");
                    if (!accesses.isEmpty()) {
                        Set<Note> set = new TreeSet<>(Comparator.comparing(Note::getId));
                        for (Access access: accesses)
                            set.add(access.getAccessReference().getNote());
                        notes.addAll(set);
                    }

                    model.addAttribute("list", notes);
                    model.addAttribute("isNotes", true);
                    break;
                default:
                    model.addAttribute("list", new LinkedList<Note>());
                    break;
            }

            return "search";
        }

        model.addAttribute("search", new SearchView());
        model.addAttribute("list", new LinkedList<Note>());
        model.addAttribute("isNotes", false);
        return "search";
    }
}
