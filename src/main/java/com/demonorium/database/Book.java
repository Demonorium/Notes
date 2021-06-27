package com.demonorium.database;

import javax.persistence.*;
import java.util.concurrent.atomic.AtomicLong;

@Entity
public class Book {
    private static final AtomicLong globalId = new AtomicLong();

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private final long id;
    private String name;
    private String author;
    private String year;
    @ManyToOne
    private User user;

    public Book() {
        id = globalId.incrementAndGet();
        user = null;
    }
    public Book(String name, String author, String year, User user) {
        this.user = user;
        this.id = globalId.incrementAndGet();
        this.name = name;
        this.author = author;
        this.year = year;
    }

    public long getId() {
        return id;
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
