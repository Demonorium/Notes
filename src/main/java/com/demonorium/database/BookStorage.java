package com.demonorium.database;

import org.springframework.stereotype.Component;


import java.util.*;

@Component
public class BookStorage {
    List<Book> storage = new LinkedList<>();

    public Book get(long id) {
        for (Book b: storage)
            if (b.getId() == id) {
                return b;
            }
        return null;
    }

    public void add(Book book) {
        storage.add(book);
    }

    public void remove(long id) {
        storage.removeIf(book -> book.getId() == id);
    }

    public List<Book> getAll() {
        return storage;
    }
}
