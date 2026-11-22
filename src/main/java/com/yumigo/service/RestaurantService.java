package com.yumigo.service;

import com.yumigo.dto.common.PageResponse;
import com.yumigo.dto.request.restaurant.CreateRestaurantRequest;
import com.yumigo.dto.request.restaurant.RestaurantStatusUpdateRequest;
import com.yumigo.dto.response.restaurant.RestaurantResponse;
import com.yumigo.dto.response.restaurant.RestaurantSummaryResponse;

import java.util.List;

public interface RestaurantService {

    RestaurantResponse create(CreateRestaurantRequest request, Long ownerId);

    PageResponse<RestaurantSummaryResponse> search(String city,String cuisineType,String name,
                                                   int page,int size);

    RestaurantResponse getById(Long id);

    RestaurantResponse approve(Long id);

    List<RestaurantResponse> getMyRestaurants(Long ownerId);

    RestaurantResponse updateStatus(Long id, RestaurantStatusUpdateRequest request);

}
