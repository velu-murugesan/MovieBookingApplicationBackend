package com.velu.MovieBookingApplication.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String desc;
    private String genre;
    private String language;
    private Integer duration;
    private LocalDate release_date;
    @OneToMany(mappedBy = "movie",fetch = FetchType.LAZY)
    private List<Show> show;

}
