package com.andvil.flight.recommendation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    @Autowired
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
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

    public FlightDTO toDTO(Flight flight) {
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

    public Flight toEntity(FlightDTO dto) {
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
