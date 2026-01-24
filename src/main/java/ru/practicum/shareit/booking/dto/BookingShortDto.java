package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingShortDto {
    Long itemId;
    LocalDateTime start;
    LocalDateTime end;
}
