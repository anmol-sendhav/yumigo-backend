package com.yumigo.service.impl;

import com.yumigo.dto.common.PageResponse;
import com.yumigo.dto.request.restaurant.CreateRestaurantRequest;
import com.yumigo.dto.request.restaurant.RestaurantStatusUpdateRequest;
import com.yumigo.dto.response.restaurant.RestaurantResponse;
import com.yumigo.dto.response.restaurant.RestaurantSummaryResponse;
import com.yumigo.entity.Restaurant;
import com.yumigo.entity.User;
import com.yumigo.enums.RestaurantStatus;
import com.yumigo.enums.Role;
import com.yumigo.enums.UserStatus;
import com.yumigo.exception.BadRequestException;
import com.yumigo.exception.ForbiddenException;
import com.yumigo.exception.ResourceNotFoundException;
import com.yumigo.mapper.RestaurantMapper;
import com.yumigo.repository.RestaurantRepository;
import com.yumigo.repository.UserRepository;
import com.yumigo.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final UserRepository userRepository;
    private final RestaurantMapper restaurantMapper;
    private final RestaurantRepository restaurantRepository;

    @Override
    public RestaurantResponse create(CreateRestaurantRequest request, Long ownerId) {
        User owner=userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " , +  ownerId));

        if(owner.getStatus() == UserStatus.BLOCKED || owner.getStatus() == UserStatus.DELETED){
            throw new ForbiddenException("This account is not allowed to create a restaurant");
        }

        if (owner.getRole() != Role.RESTAURANT_OWNER) {
            throw new ForbiddenException("Only restaurant owners can create restaurants");
        }

        if(!request.openingTime().isBefore(request.closingTime())){
            throw new BadRequestException("Opening time must be before closing time");
        }

        Restaurant restaurant = restaurantMapper.toEntity(request,owner);
        restaurant.setStatus(RestaurantStatus.PENDING_APPROVAL);
        restaurant.setAvgRating(BigDecimal.ZERO);
        restaurant.setOpen(false);

        Restaurant saved= restaurantRepository.save(restaurant);
        return restaurantMapper.toResponse(saved);
    }


    private String convertBlankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }


    @Override
    @Transactional(readOnly = true)
    public PageResponse<RestaurantSummaryResponse> search(String city, String cuisineType, String name, int page, int size) {

        String cityFilter = convertBlankToNull(city);
        String cuisineTypeFilter = convertBlankToNull(cuisineType);
        String nameFilter = convertBlankToNull(name);

        int safePage = Math.max(page,0);
        int safeSize = Math.min(Math.max(size,1),50);
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by("name"));

        Page<Restaurant> result =
                restaurantRepository.searchActive(cityFilter, cuisineTypeFilter, nameFilter, pageable);
        return  PageResponse.from(result.map(restaurantMapper::toSummary));
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantResponse getById(Long id) {
        Restaurant restaurant = restaurantRepository.findByIdAndStatus(id,RestaurantStatus.ACTIVE)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Restaurant not found with id " , + id));
        return restaurantMapper.toResponse(restaurant);
    }

    @Override
    @Transactional
    public RestaurantResponse approve(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " , + id));

        if (restaurant.getStatus() != RestaurantStatus.ACTIVE) {
            restaurant.setStatus(RestaurantStatus.ACTIVE);
            restaurantRepository.save(restaurant);
        }

        return restaurantMapper.toResponse(restaurant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponse> getMyRestaurants(Long ownerId) {
        return restaurantRepository.findByOwnerId(ownerId).stream()
                .map(restaurantMapper::toResponse)
                .toList();
    }

    private static final Map<RestaurantStatus, Set<RestaurantStatus>> ALLOWED_TRANSITIONS = Map.of(
            RestaurantStatus.PENDING_APPROVAL, Set.of(RestaurantStatus.ACTIVE),
            RestaurantStatus.ACTIVE,           Set.of(RestaurantStatus.SUSPENDED),
            RestaurantStatus.SUSPENDED,        Set.of(RestaurantStatus.ACTIVE)
    );

    @Override
    @Transactional
    public RestaurantResponse updateStatus(Long id, RestaurantStatusUpdateRequest request) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id",id));

        RestaurantStatus current = restaurant.getStatus();
        RestaurantStatus target = request.status();

        if (current == target) {
            return restaurantMapper.toResponse(restaurant);
        }

        Set<RestaurantStatus> allowed = ALLOWED_TRANSITIONS.getOrDefault(current, Set.of());
        if (!allowed.contains(target)) {
            throw new BadRequestException(
                    "Cannot change restaurant status from " + current + " to " + target);
        }

        restaurant.setStatus(target);
        if (target != RestaurantStatus.ACTIVE) {
            restaurant.setOpen(false);
        }
        return restaurantMapper.toResponse(restaurantRepository.save(restaurant));
    }
}