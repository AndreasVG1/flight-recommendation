package com.andvil.flight.recommendation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrivalTime;
    private String seatingPlan;

    @Column(columnDefinition = "TEXT")
    private String seatsJson;

    public Flight() {}

    public Flight(String airline, String flightNumber, String departure, String destination,
                  String airplane, Integer duration, Double price, LocalDateTime departureTime,
                  LocalDateTime arrivalTime, String seatingPlan, String seatsJson) {
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
        this.seatsJson = seatsJson;
    }

    public Long getId() {
        return id;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getAirplane() {
        return airplane;
    }

    public void setAirplane(String airplane) {
        this.airplane = airplane;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getSeatingPlan() {
        return seatingPlan;
    }

    public void setSeatingPlan(String seatingPlan) {
        this.seatingPlan = seatingPlan;
    }

    public String getSeatsJson() {
        return seatsJson;
    }

    public void setSeatsJson(String seatsJson) {
        this.seatsJson = seatsJson;
    }
}
