package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUsers();
    Optional<User> findUser(Long id);
    User saveUser(User user);
    User updateUser(User user);
}
