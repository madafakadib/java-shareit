package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService{

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto postItemRequest(long userId, ItemRequestShortDto itemRequestShortDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestShortDto);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequester(user);
        itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getItemRequestsByUser(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        List<ItemRequestDto> itemRequest =  itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(userId).stream().map(ItemRequestMapper::toDto).toList();
        for (ItemRequestDto itemRequests : itemRequest) {
            List<ItemDto> items = itemRepository.findAllByRequestId(itemRequests.getId()).
                    stream().
                    map(ItemMapper::toItemDto).
                    toList();
            itemRequests.setItems(items);
        }
        return itemRequest;
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        return itemRequestRepository.findAllByRequesterIdNotOrderByCreatedDesc(userId).stream().map(ItemRequestMapper::toDto).toList();
    }

    @Override
    public ItemRequestDto getItemRequestById(long userId, long itemRequestId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        ItemRequest itemRequest = itemRequestRepository.findById(itemRequestId).orElseThrow(() -> new NotFoundException("Запрос не найден"));
        ItemRequestDto itemRequestDto = ItemRequestMapper.toDto(itemRequest);
        List<ItemDto> items = itemRepository.findAllByRequestId(itemRequestId).
                stream().
                map(ItemMapper::toItemDto).
                toList();
        itemRequestDto.setItems(items);
        return itemRequestDto;
    }
}
