package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

public class BookingMapper {
    public static BookingShortDto toBookingShortDto(Booking booking) {
        return BookingShortDto.builder()
                .itemId(booking.getItem().getId())
                .start(booking.getStartDate())
                .end(booking.getEndDate())
                .build();
    }

    public static Booking toBooking(BookingShortDto bookingShortDto) {
        return Booking.builder()
                .startDate(bookingShortDto.getStart())
                .endDate((bookingShortDto.getEnd()))
                .build();
    }

    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .itemId(booking.getItem().getId())
                .start(booking.getStartDate())
                .end(booking.getEndDate())
                .status(booking.getStatus())
                .build();
    }

    public static BookingDtoForItem toBookingDtoForItem(Booking booking) {
        return BookingDtoForItem.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}
