package com.velu.MovieBookingApplication.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shows" , uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"showTime","theater_id","movie_id"}
        )
})
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime showTime;
    private Double price;

    @OneToMany(mappedBy = "show",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Booking> bookings;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="theater_id", nullable = false)
    private Theater theater;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="movie_id", nullable = false)
    private Movie movie;
}
