package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.List;

public interface BookingService {

    BookingDto saveBooking(BookingShortDto bookingShortDto, Long userId);

    BookingDto updateBookingStatus(Long bookingId, Long userId, Boolean approve);

    BookingDto findBookingById(Long bookingId, Long userId);

    List<BookingDto> findBookingsByBookerId(Long userId, String state);

    List<BookingDto> findBookingsByOwner(Long userId, String stateStr);

}
