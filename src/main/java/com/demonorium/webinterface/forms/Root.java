package com.demonorium.webinterface.forms;

import com.demonorium.database.Book;
import com.demonorium.database.User;

import java.util.List;

public class Root {
    List<Book> books;
    User user;

    public Root(List<Book> books, User user) {
        this.books = books;
        this.user = user;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
