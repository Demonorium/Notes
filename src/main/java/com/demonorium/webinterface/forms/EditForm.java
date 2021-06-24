package com.demonorium.webinterface.forms;

import com.demonorium.database.Book;

public class EditForm {
    private Long id;
    private String name;
    private String author;
    private String year;

    public EditForm() {
        id = null;
        name = "";
        author = "";
        year = "";
    }
    public EditForm(Book book) {
        id = book.getId();
        name = book.getName();
        author = book.getAuthor();
        year = book.getYear();
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
