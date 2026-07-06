package com.salim.category.dto;

import com.salim.category.entity.Category;
import com.salim.category.entity.CategoryType;

import java.time.LocalDateTime;

public record CategoryResponse(
        Long categoryId,
        CategoryType categoryType,
        String categoryName,
        String iconKey,
        LocalDateTime insertDate,
        LocalDateTime updateDate
) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getCategoryId(),
                category.getCategoryType(),
                category.getCategoryName(),
                category.getIconKey(),
                category.getInsertDate(),
                category.getUpdateDate()
        );
    }
}
