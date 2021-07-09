package com.demonorium.webinterface;

import com.demonorium.database.entity.*;
import com.demonorium.utils.AccessRights;
import com.demonorium.utils.GroupFlags;
import com.demonorium.database.StorageController;
import com.demonorium.webinterface.view.NoteView;
import com.demonorium.webinterface.view.SearchView;
import com.demonorium.webinterface.view.SimpleViewAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
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
            Principal principal, Model model,
            HttpServletRequest rq) {

        User user = storage.user.getByUsername(principal.getName());
        model.addAttribute("user", user);
        List<Group> searchGroups = storage.group.findByUserOrderByName(user);

        List<Group> groups = new ArrayList<>();
        List<Group> defFixed = new ArrayList<>();
        List<Group> fixed = new ArrayList<>();

        for (Group group : searchGroups) {
            if (group.testFlag(GroupFlags.FIXED_LEVEL_ROOT)) {
                defFixed.add(group);
            }
            else if (group.testFlag(GroupFlags.FIXED_USER)) {
                fixed.add(group);
            } else {
                groups.add(group);
            }
        }
        LinkedList<List<Group>> superGroups = new LinkedList<>();
        superGroups.add(defFixed);
        superGroups.add(fixed);
        superGroups.add(groups);
        model.addAttribute("superGroups", superGroups);

        Group currentGroup = defFixed.get(0);
        if (groups.size() > 0)
            currentGroup = groups.get(0);

        if (groupId != null) {
            Optional<Group> selected = storage.group.findById(groupId);
            if (selected.isPresent() && (selected.get().getUser() == user)) {
                currentGroup = selected.get();
            }
        }
        model.addAttribute("noAdd", currentGroup.testFlag(GroupFlags.NO_ADD));
        model.addAttribute("selectedGroup", currentGroup.getId());

        if (currentGroup.getNetGroup()) {
            TreeSet<Note> notes = new TreeSet<>(Comparator.comparing(Note::getUpdateDate).reversed());
            List<Access> accesses = storage.access.getByUser(user);
            for (Access access: accesses) {
                notes.add(access.getAccessReference().getNote());
            }
            model.addAttribute("notes", notes);
        } else {
            TreeSet<Note> notes = new TreeSet<>(Comparator.comparing(Note::getUpdateDate).reversed());
            notes.addAll(currentGroup.getNotes());
            model.addAttribute("notes", notes);
        }

        model.addAttribute("noteIsSelected", false);
        model.addAttribute("selectedNote", -1);

        Note note = null;
        if (noteId != null) {
            Optional<Note> selected = storage.note.findById(noteId);
            if (selected.isPresent())
                if (selected.get().getGroup() == currentGroup) {
                    note = selected.get();
                    model.addAttribute("noteIsSelected", true);
                    model.addAttribute("selectedNote", noteId);
                    model.addAttribute("lockEdit",     false);
                    model.addAttribute("lockDelete",   false);
                    model.addAttribute("lockShare",    false);

                    model.addAttribute("note", new NoteView(selected.get().getName(), selected.get().getContent()));
                    model.addAttribute("isOwner", true);
                } else if (currentGroup.getNetGroup()) {
                    List<Access> accesses = storage.access.getByUserAndAccessReference_NoteIs(user, selected.get());
                    if (!accesses.isEmpty()) {
                        note = selected.get();
                        boolean lockEdit = true;
                        boolean lockDelete = true;
                        boolean lockShare = true;
                        for (Access access: accesses) {
                            lockEdit &= !access.getAccessReference().testRight(AccessRights.WRITE);
                            lockDelete &= !access.getAccessReference().testRight(AccessRights.REMOVE);
                            lockShare &= !access.getAccessReference().testRight(AccessRights.SHARE);
                        }

                        model.addAttribute("noteIsSelected", true);
                        model.addAttribute("selectedNote", noteId);
                        model.addAttribute("lockEdit",     lockEdit);
                        model.addAttribute("lockDelete",   lockDelete);
                        model.addAttribute("lockShare",    lockShare);
                        model.addAttribute("note", new NoteView(selected.get().getName(), selected.get().getContent()));
                    }
                    model.addAttribute("isOwner", false);
                }
        }
        {
            String url = rq.getRequestURL().toString();
            model.addAttribute("home", url.substring(0, url.indexOf("/home")));
        }


        if (note != null) {
            NoteAccessReference reference = storage.refs.getByUser(user);
            if (reference != null) {
                model.addAttribute("shareRef", reference.getReference());
                model.addAttribute("shared", true);
            } else {
                model.addAttribute("shareRef", "");
                model.addAttribute("shared", false);
            }
        } else {
            model.addAttribute("shareRef", "");
            model.addAttribute("shared", false);
        }



        model.addAttribute("search", new SearchView());
        return "home";
    }
}
