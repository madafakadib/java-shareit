package ru.practicum.shareit.exception;

public class IternalServerException extends RuntimeException {
    public IternalServerException(String message) {
        super(message);
    }
}
