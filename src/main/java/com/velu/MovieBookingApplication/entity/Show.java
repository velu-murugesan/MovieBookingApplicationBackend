package com.velu.MovieBookingApplication.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime showTime;
    private Double price;

    @OneToMany(mappedBy = "show",fetch = FetchType.LAZY)
    private List<Booking> bookings;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="theater_id", nullable = false)
    private Theater theater;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="movie_id", nullable = false)
    private Movie movie;
}
