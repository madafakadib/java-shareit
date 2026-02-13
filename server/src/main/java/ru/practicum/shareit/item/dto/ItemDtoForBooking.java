package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDtoForBooking {
    private Long id;
    private String name;
}
