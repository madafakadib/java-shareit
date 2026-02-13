package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentShortDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> findAllItemsByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.findAllItemsByUser(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findItem(@RequestHeader ("X-Sharer-User-Id") long userId,
                                           @PathVariable long itemId) {
        return itemClient.findItem(itemId, userId);
    }

    @PostMapping
    public ResponseEntity<Object> saveItem(@RequestBody @Valid ItemDto itemDto,
                                           @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.saveItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable Long itemId,
                              @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.updateItem(itemId, itemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> saveComment(@PathVariable Long itemId,
                                              @RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestBody @Valid CommentShortDto commentShortDto) {
        return itemClient.saveComment(itemId, commentShortDto, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam String text) {
        return itemClient.searchItems(text, userId);
    }

}
