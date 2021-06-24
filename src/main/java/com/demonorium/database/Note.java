package com.demonorium.database;




//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
import java.util.Date;

//@Entity
public class Note {
//    @Id
//    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private String name;
    private String description;
    private String content;
    private Date creationDate;
    private Date editDate;

    protected Note() {}

    public Note(String name, String content) {
        this.name = name;
        this.content = content;
        this.creationDate = new Date();
        this.editDate = this.creationDate;

        if (this.content.length() > 21) {
            this.description = this.content.substring(0, 18) + "...";
        }
        else {
            this.description = this.content;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }
}
