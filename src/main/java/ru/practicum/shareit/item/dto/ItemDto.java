package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    Long id;
    String name;
    String description;
    Boolean available;
    BookingDtoForItem nextBooking;
    BookingDtoForItem lastBooking;
    List<CommentDto> comments;
}
