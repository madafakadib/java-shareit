package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
public class Item {
    Long id;
    String name;
    String description;
    Boolean isAvailable;
    User owner;
    ItemRequest request;
}
