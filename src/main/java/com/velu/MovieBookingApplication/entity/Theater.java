package com.velu.MovieBookingApplication.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"theaterName","theaterLocation","theaterScreenType"}
        )
)
@AllArgsConstructor
@NoArgsConstructor
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
    @JsonIgnore
    private List<Show> show;

    public Theater(String theaterName, String theaterLocation, String theaterScreenType, Integer theaterCapacity) {
        this.theaterName = theaterName;
        this.theaterLocation = theaterLocation;
        this.theaterScreenType = theaterScreenType;
        this.theaterCapacity = theaterCapacity;
    }
}
