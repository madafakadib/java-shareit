package ru.practicum.shareit.item.model;

import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */
public class Item {
    Long id;
    String name;
    String description;
    Boolean isAvailable;
    User owner;
    ItemRequest request;
}
