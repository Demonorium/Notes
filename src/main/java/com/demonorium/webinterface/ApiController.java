package com.demonorium.webinterface;

import com.demonorium.database.StorageController;
import com.demonorium.database.entity.Group;
import com.demonorium.database.entity.Note;
import com.demonorium.database.entity.User;
import com.demonorium.utils.GroupFlags;
import com.demonorium.webinterface.view.NoteView;
import com.demonorium.webinterface.view.SimpleViewAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@Controller
public class ApiController {
    @Autowired
    StorageController storage;

    private static boolean access(User user, Optional instance) {
        if (instance.isPresent()) {
            if (instance.get() instanceof Group)
                return access(user, (Group) instance.get());
            if (instance.get() instanceof Note)
                return access(user, (Note) instance.get());
        }
        return false;
    }
    private static boolean access(User user, Group group) {
        return group.getUser() == user;
    }


    private static boolean access(User user, Note note) {
        return access(user, note.getGroup());
    }

    @GetMapping("/request/save_note")
    public ResponseEntity<NoteView> saveNote(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("body") String body,
            Principal principal) {

        User user = storage.user.getByUsername(principal.getName());

        if (id != null) {
            Optional<Note> note = storage.note.findById(id);
            if (access(user, note)) {
                Note edit = note.get();
                edit.setName(name);
                edit.setContent(body);
                storage.updateNote(edit);
                return ResponseEntity.ok(new NoteView(edit.getName(), edit.getDescription()));
            }
        }

        return ResponseEntity.badRequest().body(new NoteView("error", "error"));
    }


    @PostMapping("/request/save/{noteId}")
    public String edit(
            @PathVariable Long noteId,
            @ModelAttribute("note") NoteView form,
                       Principal principal) {
        Optional<Note> selected = storage.note.findById(noteId);

        if (!selected.isPresent())
            return "redirect:/home";

        Note note = selected.get();
        Group group = note.getGroup();

        if (group == null)
            return "redirect:/home";

        User user = storage.user.getByUsername(principal.getName());
        if (access(user, group)) {
            note.setName(form.getName());
            note.setContent(form.getContent());
            storage.updateNote(note);
        }

        return "redirect:/home/" + group.getId() + "/" + note.getId();
    }

    @GetMapping("/request/new_note/{selectedGroup}")
    public String createNote(@PathVariable Long selectedGroup, Principal principal) {

        User user = storage.user.getByUsername(principal.getName());
        if (selectedGroup == null) {
            return "redirect:/home";
        }
        Optional<Group> group = storage.group.findById(selectedGroup);
        if (access(user, group)) {
            Note note = new Note("", "", group.get());
            storage.note.save(note);
            return "redirect:/home/"+selectedGroup+"/"+note.getId();
        }
        return "redirect:/home";
    }

    @GetMapping("/request/new_group")
    public ResponseEntity<String> createGroup(@RequestParam("name") String name, Principal principal) {
        User user = storage.user.getByUsername(principal.getName());
        if (name != null) {
            Group group = new Group(name, user);
            storage.group.save(group);
            return ResponseEntity.ok("/home/"+group.getId() + "/-1");
        }


        return ResponseEntity.badRequest().body("/home");
    }

    @GetMapping("/request/rename_group")
    public ResponseEntity<String> renameGroup(@RequestParam("id") Long id, @RequestParam("name") String name, Principal principal) {
        User user = storage.user.getByUsername(principal.getName());

        if ((id != null) && (name != null)) {
            Optional<Group> group = storage.group.findById(id);
            if (access(user, group)) {
                if (group.get().testFlag(GroupFlags.NO_RENAME))
                    return ResponseEntity.unprocessableEntity().body("NO RENAME");

                group.get().setName(name);
                storage.group.save(group.get());
                return ResponseEntity.ok(group.get().getName());
            }
            return ResponseEntity.unprocessableEntity().body("NO ACCESS");
        }

        return ResponseEntity.badRequest().body("Parameter error");
    }

    @GetMapping("/request/remove_group")
    public ResponseEntity<String> removeGroup(@RequestParam("id") Long id, Principal principal) {
        User user = storage.user.getByUsername(principal.getName());

        if (id != null) {
            Optional<Group> group = storage.group.findById(id);
            if (access(user, group)) {
                storage.group.delete(group.get());
                return ResponseEntity.ok("");
            }
        }
        return ResponseEntity.badRequest().body("/home");
    }

    @GetMapping("/request/remove_note")
    public ResponseEntity<String> removeNote(@RequestParam("id") Long id, Principal principal) {
        User user = storage.user.getByUsername(principal.getName());

        if (id != null) {
            Optional<Note> note = storage.note.findById(id);
            if (access(user, note)) {
                storage.removeNote(note.get());
                return ResponseEntity.ok("");
            }
        }

        return ResponseEntity.badRequest().body("/home");
    }

    @GetMapping("/request/share_note")
    public ResponseEntity<String> shareNote(@RequestParam("id") Long id, Principal principal) {
        User user = storage.user.getByUsername(principal.getName());

        if (id != null) {
            Optional<Note> note = storage.note.findById(id);
            if (access(user, note)) {
                String token = storage.generateAccessToken(note.get());
                note.get().setSharecode(token);
                storage.note.save(note.get());
                return ResponseEntity.ok(token);
            }
        }

        return ResponseEntity.badRequest().body("No access");
    }


    @GetMapping("/request/group_size")
    public ResponseEntity<SimpleViewAdapter<Integer>> getSize(@RequestParam("id") Long id, Principal principal) {
        User user = storage.user.getByUsername(principal.getName());

        if (id != null) {
            Optional<Group> group = storage.group.findById(id);
            if (access(user, group)) {
                return ResponseEntity.ok(new SimpleViewAdapter<>(group.get().getNotes().size()));
            }
        }

        return ResponseEntity.badRequest().body(new SimpleViewAdapter<>(-1));
    }
}
