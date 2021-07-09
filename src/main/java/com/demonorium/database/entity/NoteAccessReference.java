package com.demonorium.database.entity;

import com.demonorium.utils.AccessRights;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class NoteAccessReference {
    @Id
    String reference;
    @ManyToOne(fetch = FetchType.EAGER)
    Note note;
    Integer rights;

    @ManyToOne(fetch = FetchType.EAGER)
    User user;

    @JsonIgnore
    @OneToMany(mappedBy = "accessReference", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<Access> accesses = new HashSet<>(32);

    public NoteAccessReference() {
    }

    public void setRights(Integer rights) {
        this.rights = rights;
    }

    public Set<Access> getAccesses() {
        return accesses;
    }

    public void setAccesses(Set<Access> accesses) {
        this.accesses = accesses;
    }

    public int getRights() {
        return rights;
    }

    public void setRights(int rights) {
        this.rights = rights;
    }

    public void onRight(Integer flag) {
        this.rights |= flag;
    }
    public void offRight(Integer flag) {
        this.rights &= ~flag;
    }
    public boolean testRight(Integer flag) {
        if (flag == 0) return true;
        return (this.rights & flag) != 0;
    }
    public boolean testRight(AccessRights flag) {
        return testRight(flag.flag());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public NoteAccessReference(String reference, Note note, User user, Integer rights) {
        this.reference = reference;
        this.note = note;
        this.rights = rights;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

}
