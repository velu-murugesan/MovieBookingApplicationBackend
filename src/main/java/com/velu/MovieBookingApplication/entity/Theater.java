package com.velu.MovieBookingApplication.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String theaterName;
    private String theaterLocation;
    private String theaterScreenType;
    private Integer theaterCapacity;
    @OneToMany(mappedBy = "theater", fetch = FetchType.LAZY)
    private List<Show> show;
}
