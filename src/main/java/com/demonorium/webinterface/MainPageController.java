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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.*;

import static java.util.Collections.sort;

@Controller
public class MainPageController {

    @Autowired
    StorageController storage;

    @GetMapping("/home/{groupId}")
    String home(@PathVariable Long groupId) {
        return "redirect:-1/";
    }

    @GetMapping("/home/{groupId}/{noteId}")
    String home(
                @PathVariable(required = false) Long groupId,
                @PathVariable(required = false) Long noteId,
//                @RequestParam(value = "search",         required = false, defaultValue = "")    String search,
//                @RequestParam(value = "search_groups",  required = false, defaultValue = "on")  Boolean searchGroups,
//                @RequestParam(value = "search_notes",   required = false, defaultValue = "on")  Boolean searchNotes,
            Principal principal, Model model) {
//        if (!model.containsAttribute("search")) {
//            model.addAttribute("search", search);
//        }
//        model.addAttribute("search_groups",(boolean) searchGroups);
//        model.addAttribute("search_notes", (boolean) searchNotes);

        User user = storage.user.getByUsername(principal.getName());
        model.addAttribute("user", user);
        List<Group> groups = storage.group.findByUserOrderByName(user);
        model.addAttribute("groups", groups);

        Group currentGroup = groups.get(0);

        if (groupId != null) {
            Optional<Group> selected = storage.group.findById(groupId);
            if (selected.isPresent() && (selected.get().getUser() == user)) {
                currentGroup = selected.get();
            }
        }

        model.addAttribute("selectedGroup", currentGroup.getId());
        TreeSet<Note> notes = new TreeSet<>(Comparator.comparing(Note::getUpdateDate).reversed());
        notes.addAll(currentGroup.getNotes());
        model.addAttribute("notes", notes);

        model.addAttribute("noteIsSelected", false);
        model.addAttribute("selectedNote", -1);

        if (noteId != null) {
            Optional<Note> selected = storage.note.findById(noteId);
            if (selected.isPresent() && (selected.get().getGroup() == currentGroup)) {
                model.addAttribute("noteIsSelected", true);
                model.addAttribute("selectedNote", noteId);
                model.addAttribute("note", new NoteView(selected.get().getName(), selected.get().getContent()));
            }
        }

        return "home";
    }
}