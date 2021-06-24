package com.demonorium;

import com.demonorium.database.Book;
import com.demonorium.database.BookStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@Import(MainConfig.class)
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class);
        BookStorage storage = context.getBean(BookStorage.class);
        storage.add(new Book("АААААА", "Я", "2021"));
        storage.add(new Book("АААААА", "Я", "2021"));
    }
}
