package com.demonorium.database.entity;

import com.demonorium.utils.GroupFlags;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TABLE_GROUPS")
public class Group {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    private User user;

    @Column(nullable = false)
    private boolean netGroup = false;
    private Integer flags = 0;

    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<Note> notes = new HashSet<>(32);

    protected Group() {}

    public Group(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public void onFlag(Integer flag) {
        this.flags |= flag;
    }
    public void offFlag(Integer flag) {
        this.flags &= ~flag;
    }
    public boolean testFlag(Integer flag) {
        return (this.flags & flag) != 0;
    }
    public boolean testFlag(GroupFlags flag) {
        return testFlag(flag.flag());
    }

    public boolean getNetGroup() {
        return netGroup;
    }

    public void setNetGroup(boolean netGroup) {
        this.netGroup = netGroup;
    }

    public Integer getFlags() {
        return flags;
    }

    public void setFlags(Integer flags) {
        this.flags = flags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", user=" + user +
                '}';
    }
}
