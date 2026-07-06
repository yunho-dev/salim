package com.sallim.category.dto;

import com.sallim.category.entity.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotNull(message = "카테고리 타입은 필수입니다.")
        CategoryType categoryType,

        @NotBlank(message = "카테고리 이름은 필수입니다.")
        @Size(max = 300, message = "카테고리 이름은 300자를 초과할 수 없습니다.")
        String categoryName,

        @NotBlank(message = "아이콘은 필수입니다.")
        @Size(max = 50, message = "아이콘 키는 50자를 초과할 수 없습니다.")
        String iconKey
) {
}