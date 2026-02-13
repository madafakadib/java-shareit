package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.IternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto findBookingById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
        if (!Objects.equals(booking.getBooker().getId(), userId) && !Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new NotFoundException("Вы не можете посмотреть это бронирования");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> findBookingsByBookerId(Long userId, String stateStr) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        State state;
        try {
            state = State.valueOf(stateStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Неизвестное состояние");
        }
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;
        switch (state) {
            case ALL -> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
            case CURRENT -> bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now);
            case PAST -> bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, now);
            case FUTURE -> bookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, now);
            case WAITING -> bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
            case REJECTED -> bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
            default -> bookings = Collections.emptyList();
        }
        return bookings.stream().map(BookingMapper::toBookingDto).toList();
    }

    @Override
    public List<BookingDto> findBookingsByOwner(Long userId, String stateStr) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        State state;
        try {
            state = State.valueOf(stateStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Неизвестное состояние");
        }
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;
        switch (state) {
            case ALL -> bookings = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
            case CURRENT -> bookings = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now);
            case PAST -> bookings = bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, now);
            case FUTURE -> bookings = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, now);
            case WAITING -> bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
            case REJECTED -> bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
            default -> bookings = Collections.emptyList();
        }
        return bookings.stream().map(BookingMapper::toBookingDto).toList();
    }

    @Override
    public BookingDto saveBooking(BookingShortDto bookingShortDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(bookingShortDto.getItemId()).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь в данный момент недоступна для бронирования");
        }
        Booking booking = BookingMapper.toBooking(bookingShortDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto updateBookingStatus(Long bookingId, Long userId, Boolean approve) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Бронирование по id " + bookingId + " не найдено"));
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new IternalServerException("Изменять статус может только владелец вещи");
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new ValidationException("Бронирование уже подтверждено");
        }
        if (approve) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

}
