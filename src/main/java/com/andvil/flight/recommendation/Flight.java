package com.andvil.flight.recommendation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor
public class Flight {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id @Setter(AccessLevel.PROTECTED) Long id;

    private String airline;
    private String flightNumber;
    private String departure;
    private String destination;
    private String airplane;
    private Integer duration;
    private Double price;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrivalTime;
    private String seatingPlan;

    public Flight(String airline, String flightNumber, String departure, String destination,
                  String airplane, Integer duration, Double price, LocalDateTime departureTime,
                  LocalDateTime arrivalTime, String seatingPlan) {
        this.airline = airline;
        this.flightNumber = flightNumber;
        this.departure = departure;
        this.destination = destination;
        this.airplane = airplane;
        this.duration = duration;
        this.price = price;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.seatingPlan = seatingPlan;
    }
}
