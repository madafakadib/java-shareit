package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

//TODO добавить проверку

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto findUser(Long id) {
         User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
         return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        if (userDto.getEmail() == null) {
            throw new ValidationException("Не введен email");
        }
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new DuplicateException("Этот email уже используется");
        }
        if (!userDto.getEmail().contains("@")) {
            throw new ValidationException("Неверно введен email");
        }
        userRepository.save(UserMapper.toUser(userDto));
        return userDto;
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
                throw new ValidationException("Этот email уже используется");
            }
            user.setEmail(userDto.getEmail());
        }
        User updatedUser = userRepository.save(user);
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public UserDto deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        userRepository.deleteById(id);
        return UserMapper.toUserDto(user);
    }
}
