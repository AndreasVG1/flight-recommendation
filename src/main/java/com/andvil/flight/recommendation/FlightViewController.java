package com.andvil.flight.recommendation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class FlightViewController {

    private FlightService flightService;

    @Autowired
    public FlightViewController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/flights")
    public String flights(
            Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "departureTime,asc") String sort,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Integer duration,
            @RequestParam(required = false) String departureTime) {


        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(FlightRestController.getSortOrder(sort)));

        Page<FlightDTO> flightDTOs = flightService
                .getFilteredFlights(destination, price, duration, departureTime, pageable)
                .map(FlightService::toDTO);

        model.addAttribute("flightPage", flightDTOs);
        model.addAttribute("sort", sort);
        model.addAttribute("destination", destination);
        model.addAttribute("price", price);
        model.addAttribute("duration", duration);
        model.addAttribute("departureTime", departureTime);

        int totalPages = flightDTOs.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "flights";
    }

    @GetMapping("/flights/select")
    public String selectFlight(@RequestParam("flightNumber") String flightNumber,
                               @RequestParam (required = false) String preference,
                               Model model) {
        Flight flight = flightService.getFlightByNumber(flightNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Flight with number " + flightNumber + " not found!"));

        SelectedFlightDTO flightDTO = preference == null ? flightService.toSelectedDTO(flight, null) : flightService.toSelectedDTO(flight, preference);
        model.addAttribute("flight", flightDTO);
        model.addAttribute("preference", preference);

        return "flight";
    }

    @PostMapping("/flights/confirm-selection")
    public String confirmSelection(
            @RequestParam("flightNumber") String flightNumber,
            @RequestParam("selectedSeats") List<String> selectedSeats,
            Model model) {

        Flight flight = flightService.getFlightByNumber(flightNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Flight with number " + flightNumber + " not found!"));

        SelectedFlightDTO flightDTO = flightService.toSelectedDTO(flight, null);
        model.addAttribute("flight", flightDTO);

        //flightService.reserveSeats(flightNumber, selectedSeats);

        model.addAttribute("message", "Seats reserved successfully!");
        return "confirmation";
    }
}
