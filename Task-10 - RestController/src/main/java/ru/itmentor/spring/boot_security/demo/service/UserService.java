package ru.itmentor.spring.boot_security.demo.service;

import ru.itmentor.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User getUser(Long id);

    List<User> getAllUsers();

    User updateUser(User user, Long id);

    boolean deleteUser(Long id);
}
