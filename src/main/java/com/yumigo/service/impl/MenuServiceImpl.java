package com.yumigo.service.impl;

import com.yumigo.dto.request.menu.MenuCategoryRequest;
import com.yumigo.dto.request.menu.MenuItemAvailabilityRequest;
import com.yumigo.dto.request.menu.MenuItemRequest;
import com.yumigo.dto.response.menu.MenuCategoryResponse;
import com.yumigo.dto.response.menu.MenuCategoryWithItemsResponse;
import com.yumigo.dto.response.menu.MenuItemResponse;
import com.yumigo.dto.response.menu.RestaurantMenuResponse;
import com.yumigo.dto.response.restaurant.RestaurantResponse;
import com.yumigo.entity.MenuCategory;
import com.yumigo.entity.MenuItem;
import com.yumigo.entity.Restaurant;
import com.yumigo.enums.RestaurantStatus;
import com.yumigo.exception.BadRequestException;
import com.yumigo.exception.DuplicateResourceException;
import com.yumigo.exception.ForbiddenException;
import com.yumigo.exception.ResourceNotFoundException;
import com.yumigo.mapper.MenuMapper;
import com.yumigo.mapper.RestaurantMapper;
import com.yumigo.repository.MenuCategoryRepository;
import com.yumigo.repository.MenuItemRepository;
import com.yumigo.repository.RestaurantRepository;
import com.yumigo.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuMapper menuMapper;
    private final RestaurantMapper restaurantMapper;
    private final MenuItemRepository menuItemRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final RestaurantRepository restaurantRepository;


    private Restaurant getOwnedRestaurant(Long restaurantId, Long userId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant not found with id " , + restaurantId));

        if (!restaurant.getOwner().getId().equals(userId)) {
            throw new ForbiddenException("You do not own this restaurant");
        }
        return restaurant;
    }

    @Override
    @Transactional
    public MenuCategoryResponse createCategory(Long restaurantId, MenuCategoryRequest request, Long userId) {
        Restaurant restaurant = getOwnedRestaurant(restaurantId,userId);
        String name=request.name().trim();

    if(menuCategoryRepository.existsByRestaurantIdAndNameIgnoreCase(restaurantId,request.name())){
     throw new DuplicateResourceException("Category '" + name + "' already exists");
    }
        MenuCategory category = new MenuCategory();
        menuMapper.applyToCategory(category,request);
        category.setRestaurant(restaurant);
        return menuMapper.toCategoryResponse(menuCategoryRepository.save(category));
    }

    @Override
    @Transactional
    public MenuCategoryResponse updateCategory(Long restaurantId, Long categoryId, MenuCategoryRequest request, Long userId) {
       getOwnedRestaurant(restaurantId,userId);
        MenuCategory menuCategory = menuCategoryRepository.findByIdAndRestaurantId(categoryId,restaurantId)
        .orElseThrow(() ->
        new ResourceNotFoundException("Menu category not found with id " , +categoryId));

        if(menuCategoryRepository.existsByRestaurantIdAndNameIgnoreCaseAndIdNot(restaurantId, request.name(),userId)){
            throw new DuplicateResourceException("Category '" + request.name() + "' already exists ");
        }

        menuCategory.setName(request.name().trim());
        menuCategory.setDisplayOrder(request.displayOrder());
        return menuMapper.toCategoryResponse(menuCategoryRepository.save(menuCategory));
    }

    @Override
    @Transactional
    public void deleteCategory(Long restaurantId, Long categoryId, Long userId) {

    getOwnedRestaurant(restaurantId,userId);

    MenuCategory menuCategory = menuCategoryRepository.findByIdAndRestaurantId(categoryId,restaurantId)
    .orElseThrow(() ->
    new ResourceNotFoundException("Category not found with id '" , + userId));

    if(menuItemRepository.existsByCategoryId(categoryId)){
        throw new BadRequestException("Remove or move the its items first");
    }
    menuCategoryRepository.delete(menuCategory);
    }

    @Override
    @Transactional
    public MenuItemResponse createItem(Long restaurantId, MenuItemRequest request, Long userId) {

        Restaurant restaurant = getOwnedRestaurant(restaurantId, userId);

        MenuCategory category = menuCategoryRepository
                .findByIdAndRestaurantId(request.categoryId(), restaurantId)
                .orElseThrow(() ->
                        new BadRequestException("Category does not belong to this restaurant"));

        MenuItem item = new MenuItem();
        item.setAvailable(true);
        menuMapper.applyToItem(item, request);
        item.setRestaurant(restaurant);
        item.setCategory(category);

        return menuMapper.toItemResponse(menuItemRepository.save(item));
    }

    @Override
    @Transactional
    public MenuItemResponse updateItem(Long restaurantId, Long itemId, MenuItemRequest request, Long userId) {

        Restaurant restaurant = getOwnedRestaurant(restaurantId,userId);

        MenuCategory category = menuCategoryRepository
                .findByIdAndRestaurantId(request.categoryId(),restaurantId)
                .orElseThrow(() ->
                        new BadRequestException("Category does not belongs to this restaurant"));

        MenuItem item = menuItemRepository.findByIdAndRestaurantId(itemId,restaurantId)
        .orElseThrow(() ->
        new ResourceNotFoundException("Category not found with id '" , + itemId));

        item.setCategory(category);
        item.setName(request.name().trim());
        item.setDescription(request.description());
        item.setPrice(request.price());
        item.setVeg(request.veg());
        item.setImageUrl(request.imageUrl());
        item.setPreparationTimeMins(request.preparationTimeMins());
        if (request.available() != null) {
            item.setAvailable(request.available());
        }
        return menuMapper.toItemResponse(menuItemRepository.save(item));
    }

    @Override
    @Transactional
    public MenuItemResponse updateAvailability(Long restaurantId, Long itemId, MenuItemAvailabilityRequest request, Long userId) {

        getOwnedRestaurant(restaurantId,userId);

        MenuItem item = menuItemRepository.findByIdAndRestaurantId(itemId, restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item", itemId));

        item.setAvailable(request.available());
        return menuMapper.toItemResponse(item);
    }

    @Override
    @Transactional
    public void deleteItem(Long restaurantId, Long itemId, Long userId) {

        getOwnedRestaurant(restaurantId,userId);

        MenuItem item = menuItemRepository.findByIdAndRestaurantId(itemId,restaurantId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Menu Item Not Found" , itemId));

        menuItemRepository.delete(item);
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantMenuResponse getMenu(Long restaurantId) {

    Restaurant restaurant = restaurantRepository.findByIdAndStatus(restaurantId, RestaurantStatus.ACTIVE)
    .orElseThrow(() ->
    new ResourceNotFoundException("Restaurant not found with id : " ,restaurantId));

   List<MenuCategory> categories = menuCategoryRepository.findByRestaurantIdOrderByDisplayOrderAsc(restaurantId);
   List<MenuItem> items = menuItemRepository.findAllByRestaurantIdWithCategory(restaurantId);

   Map<Long,List<MenuItem>> itemsByCategory = items.stream()
   .collect(Collectors.groupingBy(item -> item.getCategory().getId()));

        List<MenuCategoryWithItemsResponse> categoryResponses = categories.stream()
                .map(category -> new MenuCategoryWithItemsResponse(
                        category.getId(),
                        category.getName(),
                        category.getDisplayOrder(),
                        itemsByCategory.getOrDefault(category.getId(), List.of()).stream()
                                .map(menuMapper::toItemResponse)
                                .toList()))
                .filter(response -> !response.items().isEmpty())
                .toList();

        return new RestaurantMenuResponse(
                restaurantMapper.toSummary(restaurant),
                categoryResponses);
    }
}
