package com.demonorium.database;

import java.util.concurrent.atomic.AtomicLong;

public class Book {
    private static final AtomicLong globalId = new AtomicLong();

    private final long id;
    private String name;
    private String author;
    private String year;

    public Book() {
        id = globalId.incrementAndGet();
    }
    public Book(String name, String author, String year) {
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
