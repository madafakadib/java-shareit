package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public List<ItemDto> findAllItemsByUser(Long userId) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        LocalDateTime now = LocalDateTime.now();
        List<Item> items =  itemRepository.findAllItemsByOwnerIdOrderByIdAsc(userId);
        List<ItemDto> result = new ArrayList<>();
        for (Item item : items) {
            ItemDto itemDto = ItemMapper.toItemDto(item);
            itemDto.setNextBooking(bookingRepository
                    .findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(item.getId(), Status.APPROVED, now)
                    .map(BookingMapper::toBookingDtoForItem)
                    .orElse(null));
            itemDto.setLastBooking(bookingRepository
                    .findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(item.getId(), Status.APPROVED, now)
                    .map(BookingMapper::toBookingDtoForItem)
                    .orElse(null));
            List<CommentDto> comments = commentRepository.findAllCommentByItemId(item.getId()).stream().map(CommentMapper::toCommentDto).toList();
            itemDto.setComments(comments);
            result.add(itemDto);
        }
        return result;
    }

    @Override
    public ItemDto findItem(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найден"));
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        List<CommentDto> comments = commentRepository.findAllCommentByItemId(itemId).stream().map(CommentMapper::toCommentDto).toList();
        ItemDto itemDto = ItemMapper.toItemDto(item);
        if (item.getOwner().getId().equals(owner.getId())) {
            LocalDateTime now = LocalDateTime.now();
            itemDto.setNextBooking(bookingRepository
                    .findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(itemId, Status.APPROVED, now)
                    .map(BookingMapper::toBookingDtoForItem)
                    .orElse(null));
            itemDto.setLastBooking(bookingRepository
                    .findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(itemId, Status.APPROVED, now)
                    .map(BookingMapper::toBookingDtoForItem)
                    .orElse(null));
        }
        itemDto.setComments(comments);
        return itemDto;
    }

    @Override
    @Transactional
    public ItemDto saveItem(ItemDto itemDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        System.out.println(itemDto.getRequestId());
        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestRepository.findById(itemDto.getRequestId()).orElseThrow(() -> new NotFoundException("Запрос не найден"));
            item.setRequest(itemRequest);
        }
        Item savedItem = itemRepository.save(item);
        System.out.println("DEBUG: Request in Entity is: " + savedItem.getRequest());
        if (savedItem.getRequest() != null) {
            System.out.println("DEBUG: Request ID is: " + savedItem.getRequest().getId());
        }
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещи по id " + itemId + " не найдена"));
        if (userId == null || userId == 0) {
            throw new ValidationException("Не указан id пользователя");
        }
        if (Objects.equals(item.getOwner().getId(), userId)) {
            if (itemDto.getName() != null) {
                item.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                item.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                item.setAvailable(itemDto.getAvailable());
            }
        } else {
            throw new NotFoundException("Вы не являетесь владельцем вещи");
        }

        itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.search(text).stream().map(ItemMapper::toItemDto).toList();
    }

    @Override
    public CommentDto saveComment(Long itemId, CommentShortDto commentShortDto, Long userId) {
        if (commentShortDto.getText() == null || commentShortDto.getText().isBlank()) {
            throw new ValidationException("Текст комментария не может быть пустым");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещи не найдена"));
        boolean existsBooking = bookingRepository
                .existsByItemIdAndBookerIdAndStatusAndEndBefore(itemId, userId, Status.APPROVED, LocalDateTime.now());

        if (!existsBooking) {
            throw new ValidationException("Вы не можете оставить комментарий к вещи, которую не арендовали");
        }
        Comment comment = new Comment();
        comment.setText(commentShortDto.getText());
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
    }
}
