package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> findAllUsers();

    UserDto findUser(Long id);

    UserDto saveUser(UserDto userDto);

    UserDto updateUser(Long id, UserDto userDto);

    UserDto deleteUser(Long id);

}
