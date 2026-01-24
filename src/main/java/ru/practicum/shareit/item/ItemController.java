package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.utils.Headers;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    ItemServiceImpl itemService;

    @GetMapping
    public List<ItemDto> findAllItemsByUser(@RequestHeader (Headers.USER_ID) Long userId) {
        return itemService.findAllItemsByUser(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findItem(@PathVariable Long itemId,
                            @RequestHeader (Headers.USER_ID) Long userId) {
        return itemService.findItem(itemId, userId);
    }

    @PostMapping
    public ItemDto saveItem(@RequestBody ItemDto itemDto,
                            @RequestHeader(Headers.USER_ID) Long userId) {
        return itemService.saveItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @RequestBody ItemDto itemDto,
                              @RequestHeader(Headers.USER_ID) Long userId) {
        return itemService.updateItem(itemId, itemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto saveComment(@PathVariable Long itemId,
                                  @RequestBody String text,
                                  @RequestHeader(Headers.USER_ID) Long userId) {
        return itemService.saveComment(itemId, text, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }

}
