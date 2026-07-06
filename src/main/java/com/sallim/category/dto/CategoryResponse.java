package com.sallim.category.dto;

import com.sallim.category.entity.Category;
import com.sallim.category.entity.CategoryType;

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
