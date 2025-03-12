package com.andvil.flight.recommendation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Flight {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id Long id;

    private String airline;
    private String flightNumber;
    private String departure;
    private String destination;
    private String airplane;
    private Integer duration;
    private Double price;
    private LocalDate departureTime;
    private LocalDate arrivalTime;
    private String seatingPlan;
}
