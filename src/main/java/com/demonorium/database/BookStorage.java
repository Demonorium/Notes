package com.demonorium.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;


import java.util.*;

public interface BookStorage extends CrudRepository<Book, Long> {
    Book getBookById(long id);
    List<Book> getAllByUser(User user);
}
