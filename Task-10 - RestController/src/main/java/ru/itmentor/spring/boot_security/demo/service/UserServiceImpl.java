package ru.itmentor.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.repository.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;  // добавляем

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;  // инициализируем
    }

    @Override
    public User createUser(User user) {
        // Для каждой роли проверяем, сохранена ли она уже в базе данных
        user.setRoles(user.getRoles().stream()
                .map(role -> roleService.getRoleByName(role.getName()))
                .collect(Collectors.toSet()));

        userRepository.save(user);
        return user;
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user, Long id) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            return null; // или выбрось исключение
        }

        existingUser.setName(user.getName());
        existingUser.setLastname(user.getLastname());
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword()); // если необходимо обновить пароль

        // Обновление ролей
        Set<Role> updatedRoles = user.getRoles().stream()
                .map(role -> roleService.getRoleByName(role.getName())) // Получаем существующие роли
                .collect(Collectors.toSet());

        existingUser.setRoles(updatedRoles);

        return userRepository.save(existingUser);
    }

    @Override
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}