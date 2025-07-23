//package ru.practicum.booking.model;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//import ru.practicum.booking.dto.BookingStatus;
//import ru.practicum.user.model.User;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "booking")
//@Getter
//@Setter
//@ToString
//public class Booking {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
//
//    @Column(name = "start_date")
//    private LocalDateTime startDate;
//
//    @Column(name = "end_date")
//    private LocalDateTime endDate;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "item_id")
//    @ToString.Exclude
//    private Item item;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "booker_id")
//    @ToString.Exclude
//    private User booker;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "status")
//    private BookingStatus status;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Booking)) return false;
//        return id != null && id.equals(((Booking) o).getId());
//    }
//
//    @Override
//    public int hashCode() {
//        return getClass().hashCode();
//    }
//}
