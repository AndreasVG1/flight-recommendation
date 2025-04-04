package com.andvil.flight.recommendation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    @Autowired
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    // Method that returns paged Flights based on sorting
    public Page<Flight> getFilteredFlights(String destination, Double price, Integer duration, String departureTime, Pageable pageable) {
        Specification<Flight> spec = Specification.where(null);

        if (destination != null && !destination.isBlank()) {
            spec = spec.and((root, _, cb) -> cb.equal(root.get("destination"), destination));
        }
        if (price != null) {
            spec = spec.and((root, _, cb) -> cb.lessThanOrEqualTo(root.get("price"), price));
        }
        if (duration != null) {
            spec = spec.and((root, _, cb) -> cb.lessThanOrEqualTo(root.get("duration"), duration));
        }
        if (departureTime != null) {
            try {
                LocalDateTime parsedTime = LocalDateTime.parse(departureTime);
                spec = spec.and((root, _, cb) -> cb.greaterThanOrEqualTo(root.get("departureTime"), parsedTime));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd'T'HH:mm.");
            }
        }

        return flightRepository.findAll(spec, pageable);
    }

    // Method for searching for a flight by flight number
    public Optional<Flight> getFlightByNumber(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber);
    }

    // Method for converting a database object into a data transfer object(DTO) for getting all flights
    public static FlightDTO toDTO(Flight flight) {
        return new FlightDTO(
                flight.getAirline(),
                flight.getFlightNumber(),
                flight.getAirplane(),
                flight.getDeparture(),
                flight.getDestination(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getSeatingPlan(),
                flight.getPrice(),
                flight.getDuration()
        );
    }

    // Method for converting a database object into a DTO for a specific flight
    public SelectedFlightDTO toSelectedDTO(Flight flight, String userPreferences) {
        return new SelectedFlightDTO(
                flight.getAirline(),
                flight.getFlightNumber(),
                flight.getAirplane(),
                flight.getDeparture(),
                flight.getDestination(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getPrice(),
                flight.getDuration(),
                getSeats(flight),
                recommendSeats(getSeats(flight), userPreferences),
                new String[]{}
        );
    }

    // Method for adding a flight to the database or updating an existing one
    public void save(Flight flight) {
        flightRepository.save(flight);
    }

    // Algorithm that categorizes seats into 3 categories and returns the one that matches the user preference
    private String[] recommendSeats(String[][] seatingPlan, String userPreference) {
        if (userPreference == null) {
            return new String[]{};
        }

        List<String> windowSeats = new ArrayList<>();
        List<String> nearExitSeats = new ArrayList<>();
        List<String> legRoomSeats = new ArrayList<>();

        for (int row = 0; row < seatingPlan.length; row++) {
            for (int seat = 0; seat < seatingPlan[0].length; seat++) {
                String currentSeat = seatingPlan[row][seat];

                // Window seats (first and last seat in the row)
                if ((seat == 0 || seat == seatingPlan[0].length - 1) && !currentSeat.equals("X")) {
                    windowSeats.add(currentSeat);
                }

                // Near exit seats (rows 1 to 3 inclusive)
                if (row <= 2 && !currentSeat.equals(" | ") && !currentSeat.equals("X")) {
                    nearExitSeats.add(currentSeat);
                }

                // Legroom seats (rows 8 to 12 inclusive)
                if (row >= 7 && row <= 11 && !currentSeat.equals(" | ") && !currentSeat.equals("X")) {
                    legRoomSeats.add(currentSeat);
                }
            }
        }

        return switch (userPreference) {
            case "window" -> windowSeats.toArray(new String[0]);
            case "legroom" -> legRoomSeats.toArray(new String[0]);
            case "near_exit" -> nearExitSeats.toArray(new String[0]);
            default -> new String[]{};
        };

    }

    // Method that either returns the seating plan from a flight or randomly generates the plan if one does not exist
    private String[][] getSeats(Flight flight) {
        if (flight.getSeatsJson() == null) {
            String[][] seats = generateSeating(flight.getSeatingPlan());
            flight.setSeatsJson(convertSeatsToJson(seats));
            flightRepository.save(flight);
            return seats;
        } else {
            return convertJsonToSeats(flight.getSeatsJson());
        }
    }

    // Method that generates the seating plan, gives each seat a name and occupies random seats
    private String[][] generateSeating(String seatingPlan) {
        String[] parts = seatingPlan.split("-");
        int leftSeats = Integer.parseInt(parts[0]);
        int rightSeats = Integer.parseInt(parts[1]);
        int rows = 20;

        int totalSeatsPerRow = leftSeats + rightSeats + 1;
        String[][] seatPlan = new String[rows][totalSeatsPerRow];

        // Algorithm generated by ChatGPT
        for (int row = 0; row < rows; row++) {
            for (int seat = 0; seat < totalSeatsPerRow; seat++) {
                if (seat == leftSeats) {
                    seatPlan[row][seat] = " | ";
                } else {
                    char seatLetter = (char) ('A' + (seat > leftSeats ? seat - 1 : seat));
                    seatPlan[row][seat] = (Math.random() < 0.2) ? "X" : (row + 1) + String.valueOf(seatLetter);
                }
            }
        }

        return seatPlan;
    }

    // Method generated by ChatGPT
    public String convertSeatsToJson(String[][] seats) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(seats);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting seats to JSON", e);
        }
    }

    // Method generated by ChatGPT
    private String[][] convertJsonToSeats(String seatsJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(seatsJson, String[][].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to seats", e);
        }
    }
}
