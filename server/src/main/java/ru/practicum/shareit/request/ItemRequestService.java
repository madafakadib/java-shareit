package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;

import java.awt.print.Pageable;
import java.util.List;

public interface ItemRequestService {
    ItemRequestDto postItemRequest(long userId, ItemRequestShortDto itemRequestShortDto);
    List<ItemRequestDto> getItemRequestsByUser(long userId);
    List<ItemRequestDto> getAllItemRequests(long userId);
    ItemRequestDto getItemRequestById(long userId, long itemRequestId);
}
