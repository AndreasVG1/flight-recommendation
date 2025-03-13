package com.andvil.flight.recommendation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightRestController {

    private final FlightService flightService;

    @Autowired
    public FlightRestController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public ResponseEntity<Page<FlightDTO>> getAllFlights(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "departureTime,asc") String sort) {

        List<Sort.Order> orders = new ArrayList<>();
        String[] sortParams = sort.split(",");

        if (sortParams.length > 0) {
            String property = sortParams[0].trim();
            Sort.Direction direction = (sortParams.length > 1 && sortParams[1].trim().equalsIgnoreCase("desc"))
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;

            orders.add(new Sort.Order(direction, property));
        }

        Sort sortConfig = Sort.by(orders);
        Pageable pageable = PageRequest.of(page, size, sortConfig);
        Page<Flight> flightPage = flightService.getAllFlights(pageable);
        Page<FlightDTO> flightDTOs = flightPage.map(FlightService::toDTO);

        return ResponseEntity.status(HttpStatus.OK).body(flightDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightDTO> getFlightById(@PathVariable Long id) {
        Flight flight = flightService.getFlight(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight with ID " + id + " not found!"));
        return ResponseEntity.status(HttpStatus.OK).body(FlightService.toDTO(flight));
    }
}
