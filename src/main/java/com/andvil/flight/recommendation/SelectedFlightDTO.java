package com.andvil.flight.recommendation;

import java.time.LocalDateTime;

public record SelectedFlightDTO(
        String airline,
        String flightNumber,
        String airplane,
        String departure,
        String destination,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        Double price,
        Integer duration,
        String[][] seatingPlan,
        String[] recommendedSeats,
        String[] selectedSeats
) {
}
