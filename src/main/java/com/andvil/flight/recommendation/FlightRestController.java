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
import java.util.Arrays;
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
            @RequestParam(defaultValue = "departureTime,asc") String sort,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Integer duration,
            @RequestParam(required = false) String departureTime) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(getSortOrder(sort)));

        Page<FlightDTO> flightDTOs = flightService
                .getFilteredFlights(destination, price, duration, departureTime, pageable)
                .map(FlightService::toDTO);

        return ResponseEntity.status(HttpStatus.OK).body(flightDTOs);
    }

    @GetMapping("/{flightNumber}")
    public ResponseEntity<SelectedFlightDTO> getFlightByNumber(@PathVariable String flightNumber,
                                                               @RequestParam(required = false) String preference) {
        Flight flight = flightService.getFlightByNumber(flightNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Flight with number " + flightNumber + " not found!"));

        List<String> availablePreferences = new ArrayList<>(Arrays.asList("window", "near_exit", "legroom"));

        if (preference != null) {
            SelectedFlightDTO selectedFlightDTO = flightService.toSelectedDTO(flight, preference);

            if (!availablePreferences.contains(preference)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            return ResponseEntity.status(HttpStatus.OK).body(selectedFlightDTO);
        }

        return ResponseEntity.status(HttpStatus.OK).body(flightService.toSelectedDTO(flight, null));
    }

    @PostMapping("/{flightNumber}/select")
    public ResponseEntity<SelectedFlightDTO> reserveSeats(@PathVariable String flightNumber, @RequestParam String seats) {
        Flight flight = flightService.getFlightByNumber(flightNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found!"));

        String[] selectedSeats = seats.split(",");

        SelectedFlightDTO flightDTO = flightService.toSelectedDTO(flight, null);

        String[][] seatingPlan = flightDTO.seatingPlan();

        for (String selectedSeat : selectedSeats) {
            boolean seatFound = false;

            for (int row = 0; row < seatingPlan.length; row++) {
                for (int col = 0; col < seatingPlan[row].length; col++) {
                    if (selectedSeat.equals(seatingPlan[row][col])) {
                        seatingPlan[row][col] = "O";
                        seatFound = true;
                        break;
                    }
                }
            }

            if (!seatFound) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(flightDTO);
            }
        }

        flight.setSeatsJson(flightService.convertSeatsToJson(seatingPlan));
        flightService.save(flight);
        SelectedFlightDTO selectedFlightDTO = flightService.toSelectedDTO(flight, null);

        return ResponseEntity.status(HttpStatus.OK).body(selectedFlightDTO);
    }

    private List<Sort.Order> getSortOrder(String sort) {
        String[] sortParams = sort.split(",");
        String property = sortParams[0].trim();
        Sort.Direction direction = (sortParams.length > 1 && sortParams[1].trim().equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return List.of(new Sort.Order(direction, property));
    }
}
