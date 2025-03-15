package com.andvil.flight.recommendation;

public record SelectedFlightDTO(
        String airline,
        String flightNumber,
        String airplane,
        String departure,
        String destination,
        String departureTime,
        String arrivalTime,
        Double price,
        Integer duration,
        String[][] seatingPlan
) {
}
