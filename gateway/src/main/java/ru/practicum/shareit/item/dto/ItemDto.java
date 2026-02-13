package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @NotBlank(message = "Описание не может быть пустым")
    private String description;
    @NotNull(message = "Доступность не может быть пустым")
    private Boolean available;
    private Long requestId;
    private BookingDtoForItem nextBooking;
    private BookingDtoForItem lastBooking;
    private List<CommentDto> comments;
}
