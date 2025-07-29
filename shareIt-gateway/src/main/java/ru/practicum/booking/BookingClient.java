package ru.practicum.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.BaseClient;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareIt-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public ResponseEntity<Object> postBooking(Long userId, NewBookingDto request) {
        return post("", userId, request);
    }

    public ResponseEntity<Object> patchApproved(Long userId, Long bookingId, String approved) {
        return patch("/" + bookingId + "?approved=" + approved, userId, approved);
    }

    public ResponseEntity<Object> getInfo(Long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getBookings(Long userId, String state) {
        return get("?state=" + state, userId);
    }

    public ResponseEntity<Object> getBookingOwner(Long userId, String state) {
        return get("/owner?state=" + state, userId);
    }
}