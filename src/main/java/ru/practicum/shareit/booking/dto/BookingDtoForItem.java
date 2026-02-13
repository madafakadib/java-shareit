package ru.practicum.shareit.booking.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDtoForItem {
    Long id;
    Long bookerId;
}
