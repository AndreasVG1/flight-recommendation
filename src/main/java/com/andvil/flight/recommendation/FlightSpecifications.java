package com.andvil.flight.recommendation;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class FlightSpecifications {
    public static Specification<Flight> hasDestination(String destination) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("destination"), destination);
    }

    public static Specification<Flight> hasPrice(Double price) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), price);
    }

    public static Specification<Flight> hasDuration(Integer duration) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("duration"), duration);
    }

    public static Specification<Flight> hasDepartureTime(LocalDateTime departureTime) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("departureTime"), departureTime);
    }
}
