package com.demonorium.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TABLE_NOTES")
public class Note{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(length = 20000)
    private String content;

    private String description;

    private Date creationDate;
    private Date updateDate;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="group_id", nullable = false)
    private Group group;

    @JsonIgnore
    @OneToMany(mappedBy = "note", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<NoteAccessReference> accesses = new HashSet<>(32);


    protected Note() {}

    public Note(String name, String content, Group group) {
        this.name = name;
        this.content = content;
        this.creationDate = new Date();
        this.updateDate = this.creationDate;
        if (content.length() > 23)
            description = content.substring(0, 20) + "...";
        else
            description = content;

        this.group = group;
    }

    public Set<NoteAccessReference> getAccesses() {
        return accesses;
    }

    public void setAccesses(Set<NoteAccessReference> accesses) {
        this.accesses = accesses;
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

    public String getContent() {
        return content;
    }

    public String getFormattedDate() {
        return new SimpleDateFormat("yyyy.MM.dd| HH:mm:ss").format(updateDate);
    }

    public void setContent(String content) {
        this.content = content;
        if (content.length() > 23)
            description = content.substring(0, 20) + "...";
        else
            description = content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Note{" +
                "\n\tid=" + id +
                ",\n\t name='" + name + '\'' +
                ",\n\t content='" + content + '\'' +
                ",\n\t creationDate=" + creationDate +
                ",\n\t updateDate=" + updateDate +
                ",\n\t group=" + group +
                "\n}";
    }
}
