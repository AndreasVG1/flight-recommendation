package com.andvil.flight.recommendation;

public record FlightDTO(
        String airline,
        String flightNumber,
        String airplane,
        String departure,
        String destination,
        String departureTime,
        String arrivalTime,
        String seatingPlan,
        Double price,
        Integer duration
) {}
