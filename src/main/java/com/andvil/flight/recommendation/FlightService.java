package com.andvil.flight.recommendation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    @Autowired
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public Page<Flight> getFilteredFlights(String destination, Double price, Integer duration, String departureTime, Pageable pageable) {
        Specification<Flight> spec = Specification.where(null);

        if (destination != null && !destination.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("destination"), destination));
        }
        if (price != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), price));
        }
        if (duration != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("duration"), duration));
        }
        if (departureTime != null) {
            try {
                LocalDateTime parsedTime = LocalDateTime.parse(departureTime);
                spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("departureTime"), parsedTime));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd'T'HH:mm.");
            }
        }

        return flightRepository.findAll(spec, pageable);
    }

    public Optional<Flight> getFlight(Long id) {
        return flightRepository.findById(id);
    }

    public Flight addFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public void deleteFlight(Long id) {
        if (flightRepository.existsById(id)) {
            flightRepository.deleteById(id);
        } else {
            throw new RuntimeException("Flight with id " + id + " not found!");
        }
    }

    public Flight addFromDTO(FlightDTO dto) {
        Flight flight = toEntity(dto);
        return addFlight(flight);
    }

    public static FlightDTO toDTO(Flight flight) {
        return new FlightDTO(
                flight.getAirline(),
                flight.getFlightNumber(),
                flight.getAirplane(),
                flight.getDeparture(),
                flight.getDestination(),
                flight.getDepartureTime().toString(),
                flight.getArrivalTime().toString(),
                flight.getSeatingPlan(),
                flight.getPrice(),
                flight.getDuration()
        );
    }

    public static Flight toEntity(FlightDTO dto) {
        return new Flight(
                dto.airline(),
                dto.flightNumber(),
                dto.departure(),
                dto.destination(),
                dto.airplane(),
                dto.duration(),
                dto.price(),
                LocalDateTime.parse(dto.departureTime()),
                LocalDateTime.parse(dto.arrivalTime()),
                dto.seatingPlan()
        );
    }
}
