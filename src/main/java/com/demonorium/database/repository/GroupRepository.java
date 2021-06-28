package com.demonorium.database.repository;

import com.demonorium.database.entity.Group;
import com.demonorium.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GroupRepository extends CrudRepository<Group, Long> {
    List<Group> getByUser(User user);
    Group getByName(String name);
    Group getById(Long id);
}
