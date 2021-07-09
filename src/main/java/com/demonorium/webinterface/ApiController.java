package com.demonorium.webinterface;

import com.demonorium.database.StorageController;
import com.demonorium.database.entity.*;
import com.demonorium.utils.AccessRights;
import com.demonorium.utils.GroupFlags;
import com.demonorium.webinterface.view.NoteView;
import com.demonorium.webinterface.view.SimpleViewAdapter;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@Controller
public class ApiController {
    @Autowired
    StorageController storage;

    private int getRights(User user, Note note) {
        List<Access> accesses = storage.access.getByUserAndAccessReference_NoteIs(user, note);
        return getRights(accesses);
    }
    private int getRights(List<Access> accesses) {
        int rights = 0;
        for (Access access: accesses) {
            rights |= access.getAccessReference().getRights();
        }
        return rights;
    }

    private boolean access(User user, Optional instance, int group, int note) {
        if (instance.isPresent()) {
            if (instance.get() instanceof Group)
                return access(user, (Group) instance.get(), group);
            if (instance.get() instanceof Note)
                return access(user, (Note) instance.get(), group, note);
        }
        return false;
    }
    private boolean access(User user, Optional instance, int group) {
        return access(user, instance, group, 0);
    }
    private boolean access(User user, Group group, int rights) {
        return (group.getUser() == user) && !group.testFlag(rights);
    }


    private boolean access(User user, Note note, int group, int rights) {
        if (access(user, note.getGroup(), group)) {
            return true;
        }

        List<Access> accesses = storage.access.getByUserAndAccessReference_NoteIs(user, note);
        if (accesses.isEmpty())
            return false;
        if (rights == 0)
            return true;

        //Если пересещение имеющихся и требуемых прав
        //совпадает с требуемым, то прав достаточно
        return (getRights(accesses) & rights) == rights;
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
            if (access(user, note, 0)) {
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
        if (access(user, note, 0, AccessRights.WRITE.flag())) {
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
        if (access(user, group, GroupFlags.NO_ADD.flag())) {
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
            if (access(user, group, GroupFlags.NO_RENAME.flag())) {

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
            if (access(user, group, GroupFlags.DEFAULT.flag())) {
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
            if (access(user, note, 0, AccessRights.REMOVE.flag())) {
                storage.removeNote(note.get());
                return ResponseEntity.ok("");
            }
        }

        return ResponseEntity.badRequest().body("/home");
    }

    @GetMapping("/request/hide_note")
    public ResponseEntity<SimpleViewAdapter<String>> hideNote(@RequestParam("id") Long id, Principal principal) {
        User user = storage.user.getByUsername(principal.getName());
        Optional<Note> note = storage.note.findById(id);
        if (note.isPresent()) {
            if (note.get().getGroup().getUser() == user) {
                storage.refs.deleteAll(storage.refs.getByNote(note.get()));
                return ResponseEntity.ok(new SimpleViewAdapter<>(id.toString()));
            }
        }
        return ResponseEntity.badRequest().body(new SimpleViewAdapter<>("No access"));
    }

    @GetMapping("/request/deref_note")
    public ResponseEntity<SimpleViewAdapter<String>> derefNote(@RequestParam("id") Long id, Principal principal) {
        User user = storage.user.getByUsername(principal.getName());
        Optional<Note> note = storage.note.findById(id);
        if (note.isPresent()) {
            NoteAccessReference reference = storage.refs.getByUserAndNote(user, note.get());
            if (reference != null) {
                String token = reference.getReference();
                storage.refs.delete(reference);
                return ResponseEntity.badRequest().body(new SimpleViewAdapter<>(token));
            }
        }
        return ResponseEntity.badRequest().body(new SimpleViewAdapter<>("No access"));
    }
    @GetMapping("/request/share_note")
    public ResponseEntity<SimpleViewAdapter<String>> shareNote(
            @RequestParam("id")     Long id,
            @RequestParam("write")  Boolean write,
            @RequestParam("remove") Boolean remove,
            @RequestParam("share")  Boolean share,
            Principal principal) {
        User user = storage.user.getByUsername(principal.getName());

        if (id != null) {
            Optional<Note> note = storage.note.findById(id);
            int rights = (write? AccessRights.WRITE.flag() : 0)
                    |   (remove? AccessRights.REMOVE.flag() : 0)
                    |   (share? AccessRights.SHARE.flag() : 0);
            if (access(user, note, 0, AccessRights.SHARE.flag() | rights)) {
                NoteAccessReference reference = storage.refs.getByUserAndNote(user, note.get());
                if (reference != null)
                    storage.refs.delete(reference);

                String token = storage.generateAccessToken(note.get(), rights);
                reference = new NoteAccessReference(token, note.get(), user, rights);
                storage.refs.save(reference);
                return ResponseEntity.ok(new SimpleViewAdapter<>(token));
            }
        }

        return ResponseEntity.badRequest().body(new SimpleViewAdapter<>("No access"));
    }


    @GetMapping("/request/group_size")
    public ResponseEntity<SimpleViewAdapter<Integer>> getSize(@RequestParam("id") Long id, Principal principal) {
        User user = storage.user.getByUsername(principal.getName());

        if (id != null) {
            Optional<Group> group = storage.group.findById(id);
            if (access(user, group, 0)) {
                return ResponseEntity.ok(new SimpleViewAdapter<>(group.get().getNotes().size()));
            }
        }

        return ResponseEntity.badRequest().body(new SimpleViewAdapter<>(-1));
    }
}
