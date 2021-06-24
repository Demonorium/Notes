package com.demonorium.database;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
    User getByEmail(String email);
}
