package com.demonorium.database.entity;


import com.demonorium.utils.AccessRights;

import javax.persistence.*;

@Entity
public class Access {
    @Id
    private String code;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    private Note note;

    private int rights;

    public Access() {
    }

    public Access(String code, User user, Note note) {
        this.code = code;
        this.user = user;
        this.note = note;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }
}
