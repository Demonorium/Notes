package com.demonorium.database.entity;


import com.demonorium.utils.AccessRights;

import javax.persistence.*;

@Entity
public class Access {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    private NoteAccessReference accessReference;


    public Access() {
    }

    public Access(User user, NoteAccessReference reference) {
        this.user = user;
        this.accessReference = reference;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public NoteAccessReference getAccessReference() {
        return accessReference;
    }

    public void setAccessReference(NoteAccessReference accessReference) {
        this.accessReference = accessReference;
    }
}
