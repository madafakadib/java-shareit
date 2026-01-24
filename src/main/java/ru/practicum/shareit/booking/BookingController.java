package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.utils.Headers;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingDto findBookingById(@PathVariable Long bookingId,
                                      @RequestHeader(Headers.USER_ID) Long userId) {
        return bookingService.findBookingById(bookingId,userId);
    }

    @GetMapping
    public List<BookingDto> findBookingsByUserId(@RequestHeader(Headers.USER_ID) Long userId,
                                                 @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findBookingsByBookerId(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> findBookingsByOwner(@RequestHeader (Headers.USER_ID) Long userId,
                                                @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findBookingsByOwner(userId, state);
    }

    @PostMapping
    public BookingDto createBooking(@RequestBody BookingShortDto bookingShortDto,
                                    @RequestHeader(Headers.USER_ID) Long userId) {
        return bookingService.saveBooking(bookingShortDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingStatus(@PathVariable Long bookingId,
                                          @RequestHeader(Headers.USER_ID) Long userId,
                                          @RequestParam Boolean approved) {
        return bookingService.updateBookingStatus(bookingId, userId, approved);
    }

}
