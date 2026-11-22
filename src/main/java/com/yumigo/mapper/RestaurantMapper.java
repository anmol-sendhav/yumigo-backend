package com.yumigo.mapper;

import com.yumigo.dto.request.restaurant.CreateRestaurantRequest;
import com.yumigo.dto.response.restaurant.RestaurantResponse;
import com.yumigo.dto.response.restaurant.RestaurantSummaryResponse;
import com.yumigo.entity.Restaurant;
import com.yumigo.entity.User;
import com.yumigo.enums.RestaurantStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RestaurantMapper {

    public RestaurantResponse toResponse(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getPhone(),
                restaurant.getCuisineType(),
                restaurant.getAddressLine(),
                restaurant.getCity(),
                restaurant.getState(),
                restaurant.getPinCode(),
                restaurant.getOpeningTime(),
                restaurant.getClosingTime(),
                restaurant.isOpen(),
                restaurant.getLatitude(),
                restaurant.getLongitude(),
                restaurant.getMinOrderAmount(),
                restaurant.getAvgRating(),
                restaurant.getStatus(),
                restaurant.getOwner().getId(),
                restaurant.getCreatedAt()
        );
    }

    public RestaurantSummaryResponse toSummary(Restaurant restaurant){
        return new RestaurantSummaryResponse(
                Math.toIntExact(restaurant.getId()),
                restaurant.getName(),
                restaurant.getCuisineType(),
                restaurant.getCity(),
                restaurant.getAvgRating(),
                restaurant.isOpen(),
                restaurant.getMinOrderAmount(),
                restaurant.getOpeningTime(),
                restaurant.getClosingTime()
        );
    }

    public Restaurant toEntity(CreateRestaurantRequest request, User owner) {
        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(owner);
        restaurant.setName(request.name().trim());
        restaurant.setDescription(request.description());
        restaurant.setPhone(request.phone());
        restaurant.setCuisineType(request.cuisineType().trim());
        restaurant.setAddressLine(request.addressLine().trim());
        restaurant.setCity(request.city().trim());
        restaurant.setState(request.state().trim());
        restaurant.setPinCode(request.pinCode());
        restaurant.setOpeningTime(request.openingTime());
        restaurant.setClosingTime(request.closingTime());
        restaurant.setLatitude(request.latitude());
        restaurant.setLongitude(request.longitude());
        restaurant.setMinOrderAmount(request.minOrderAmount());
        restaurant.setOpen(false);
        restaurant.setAvgRating(BigDecimal.ZERO);
        restaurant.setStatus(RestaurantStatus.PENDING_APPROVAL);
        return restaurant;
    }
    }
