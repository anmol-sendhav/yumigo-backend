package com.yumigo.dto.request.menu;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record MenuCategoryRequest(

        @NotBlank(message = "Enter a valid menu category")
        @Size(max=100,message = "name is required")
        String name,

        @PositiveOrZero
        Integer displayOrder

) {
}
