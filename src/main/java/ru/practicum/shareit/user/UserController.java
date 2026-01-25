package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public List<UserDto> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable Long id) {
        return userService.findUser(id);
    }

    @PostMapping
    public UserDto saveUser(@RequestBody UserDto userDto) {
        return userService.saveUser(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody UserDto userDto,
                              @PathVariable Long id) {
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public UserDto deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
