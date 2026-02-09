package com.velu.MovieBookingApplication.entity;
import com.velu.MovieBookingApplication.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(
        name = "bookings",
        indexes = {
                @Index(name = "idx_booking_user_id",columnList = "user_id")
        }
)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer numberOfSeats;
    private LocalDateTime bookingDate;
    private Double price;
    private BookingStatus bookingStatus;
    private LocalDateTime createdAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "booking_seat_numbers" , joinColumns = @JoinColumn(name ="booking_id"))
    @Column(name = "seat_number")
    private List<String> seatNumbers;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id" , nullable = false )
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "show_id" , nullable = false)
    private Show show;

}
