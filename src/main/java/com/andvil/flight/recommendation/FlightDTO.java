package com.andvil.flight.recommendation;

import java.time.LocalDateTime;

public record FlightDTO(
        String airline,
        String flightNumber,
        String airplane,
        String departure,
        String destination,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        String seatingPlan,
        Double price,
        Integer duration
) {}
