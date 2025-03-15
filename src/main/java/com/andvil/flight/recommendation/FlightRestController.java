package com.andvil.flight.recommendation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Random;

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
    public ResponseEntity<SelectedFlightDTO> getFlightByNumber(@PathVariable String flightNumber) {
        Flight flight = flightService.getFlightByNumber(flightNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Flight with number " + flightNumber + " not found!"));

        SelectedFlightDTO selectedFlightDTO = new SelectedFlightDTO(
                flight.getAirline(),
                flight.getFlightNumber(),
                flight.getAirplane(),
                flight.getDeparture(),
                flight.getDestination(),
                flight.getDepartureTime().toString(),
                flight.getArrivalTime().toString(),
                flight.getPrice(),
                flight.getDuration(),
                generateSeatingPlan(flight.getSeatingPlan())
        );

        return ResponseEntity.status(HttpStatus.OK).body(selectedFlightDTO);
    }

    private String[][] generateSeatingPlan(String seatingPlan) {
        String[] parts = seatingPlan.split("-");
        int leftSeats = Integer.parseInt(parts[0]);
        int rightSeats = Integer.parseInt(parts[1]);
        int rows = (leftSeats + rightSeats) * 10;

        int totalSeatsPerRow = leftSeats + rightSeats + 1;
        String[][] seatPlan = new String[rows][totalSeatsPerRow];

        for (int row = 0; row < rows; row++) {
            for (int seat = 0; seat < totalSeatsPerRow; seat++) {
                if (seat == leftSeats) {
                    seatPlan[row][seat] = " | ";
                } else {
                    int seatNumber = (seat < leftSeats) ? seat : seat - 1;
                    char seatLetter = (char) ('A' + seatNumber);
                    seatPlan[row][seat] = (row + 1) + String.valueOf(seatLetter);
                }
            }
        }

        return occupyRandomSeats(seatPlan);
    }

    private String[][] occupyRandomSeats(String[][] seatingPlan) {
        Random random = new Random();
        int rows = seatingPlan.length;
        int seats = seatingPlan[0].length;

        for (int i = 0; i < 30; i++) {
            int randomRow = random.nextInt(rows);
            int randomSeat = random.nextInt(seats);
            if (!seatingPlan[randomRow][randomSeat].equals(" | ")) {
                seatingPlan[randomRow][randomSeat] = "X";
            }
        }
        return seatingPlan;
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
