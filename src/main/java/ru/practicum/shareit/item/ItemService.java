package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> findAllItemsByUser(Long userId);

    ItemDto findItem(Long itemId, Long userId);

    ItemDto saveItem(ItemDto itemDto, Long userId);

    ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId);

    List<ItemDto> searchItems(String text);

    CommentDto saveComment(Long itemId, String text, Long userId);

}
