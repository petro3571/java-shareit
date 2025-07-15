package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotFoundUserException;
import ru.practicum.shareit.item.NewItemRepo;
import ru.practicum.shareit.user.NewUserRepo;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {
    private final NewUserRepo userRepository;
    private final NewItemRepo itemRepository;
    private final BookingRepository repository;

    @Override
    public BookingDto postBooking(Long userId, NewBookingRequest request) {
        if (!itemRepository.findAll().stream().filter(item -> item.getId().equals(request.getItemId()))
                .findFirst().isPresent()) {
            throw new NotFoundUserException("Вещи с id = " + request.getItemId() + " нет.");
        }
        Booking booking = BookingMapper.mapToBooking(request);

        if (!userRepository.findAll().stream().filter(user -> user.getId().equals(userId)).findFirst().isPresent()) {
            throw new NotFoundException("Такого Пользователя нет.");
        } else {
            booking.setBooker(userRepository.findAll().stream()
                    .filter(user -> user.getId().equals(userId)).findFirst().get());
        }

        if (!itemRepository.findAll().stream().filter(i -> i.getId().equals(request.getItemId()) && i.isAvailable())
                .findFirst().isPresent()) {
            throw new NotFoundException("Эта вещь недоступна для аренды");
        } else {
            booking.setItem(itemRepository.getById(request.getItemId()));
        }

        return BookingMapper.mapToBookingDto(repository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto patchApproved(Long userId, Long bookingId, Boolean approved) {
        Booking booking = repository.getById(bookingId);

        if (booking.equals(null)) {
            throw new NotFoundException("Аренды нет.");
        }

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId
                    + " не является владельцем вещи для подтверждения аренды.");
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.mapToBookingDto(repository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getInfo(Long userId, Long bookingId) {
        Booking booking = repository.getById(bookingId);

        if (booking.equals(null)) {
            throw new NotFoundException("Аренды нет.");
        }

        if (booking.getBooker().getId().equals(userId) || booking.getItem().getOwner().getId().equals(userId)) {
            return  BookingMapper.mapToBookingDto(booking);
        } else {
            throw new NotFoundException("Пользователь с id = " + userId
                    + " не является автором бронирования или владельцем вещи.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getBookings(Long userId, String state) {
        if (!userRepository.findAll().stream().filter(user -> user.getId().equals(userId)).findFirst().isPresent()) {
            throw new NotFoundException("Такого Пользователя нет.");
        }

            switch (state.toUpperCase()) {
                case "ALL":
                    return repository.findByBooker_Id(userId).stream().map(BookingMapper::mapToBookingDto).toList();
                case "CURRENT":
                    return repository.findByBookerIdAndStartDateBeforeAndEndDateAfter(userId, LocalDateTime.now(),
                                    LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start")).stream()
                            .map(BookingMapper::mapToBookingDto).toList();
                case "PAST":
                    return repository.findByEndDateBefore(LocalDateTime.now()).stream()
                            .filter(booking -> booking.getBooker().getId().equals(userId))
                            .map(BookingMapper::mapToBookingDto).toList();
                case "FUTURE":
                    return repository.findByEndDateAfter(LocalDateTime.now()).stream()
                            .filter(booking -> booking.getBooker().getId().equals(userId))
                            .map(BookingMapper::mapToBookingDto).toList();
                case "WAITING":
                    return repository.findByStatusLike(BookingStatus.WAITING).stream()
                            .filter(booking -> booking.getBooker().getId().equals(userId))
                            .map(BookingMapper::mapToBookingDto).toList();
                case "REJECTED":
                    return repository.findByStatusLike(BookingStatus.REJECTED).stream()
                            .filter(booking -> booking.getBooker().getId().equals(userId))
                            .map(BookingMapper::mapToBookingDto).toList();
                default:
                    throw new NotFoundException("Неизвестный тип анренды");
            }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getBookingOwner(Long userId, String state) {
        if (userRepository.getById(userId).equals(null)) {
            throw new NotFoundException("Такого Пользователя нет.");
        }

        if (itemRepository.findAll().stream().filter(i -> i.getOwner().getId().equals(userId)).toList().size() < 1) {
            return Collections.emptyList();
        }

        List<Long> itemIds = itemRepository.findAll().stream().filter(i -> i.getOwner().getId().equals(userId))
                .map(item -> item.getId())
                .toList();

        switch (state.toUpperCase()) {
            case "ALL":
                return repository.findByItemIdIn(itemIds).stream().map(BookingMapper::mapToBookingDto).toList();

            case "CURRENT":
                return repository.findByBookerIdAndStartDateBeforeAndEndDateAfter(userId, LocalDateTime.now(),
                        LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start")).stream()
                        .map(BookingMapper::mapToBookingDto).toList();
            case "PAST":
                return repository.findByEndDateBefore(LocalDateTime.now()).stream()
                        .filter(booking -> booking.getBooker().getId().equals(userId))
                        .map(BookingMapper::mapToBookingDto).toList();
            case "FUTURE":
                return repository.findByEndDateAfter(LocalDateTime.now()).stream()
                        .filter(booking -> booking.getBooker().getId().equals(userId))
                        .map(BookingMapper::mapToBookingDto).toList();
            case "WAITING":
                return repository.findByStatusLike(BookingStatus.WAITING).stream()
                        .filter(booking -> booking.getBooker().getId().equals(userId))
                        .map(BookingMapper::mapToBookingDto).toList();
            case "REJECTED":
                return repository.findByStatusLike(BookingStatus.REJECTED).stream()
                        .filter(booking -> booking.getBooker().getId().equals(userId))
                        .map(BookingMapper::mapToBookingDto).toList();
            default:
                throw new NotFoundException("Неизвестный тип анренды");
        }
    }
}