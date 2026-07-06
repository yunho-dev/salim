package com.salim.category.controller;

import com.salim.category.dto.CategoryRequest;
import com.salim.category.dto.CategoryResponse;
import com.salim.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor // 생성자 주입
public class CategoryApiController {

    private final CategoryService categoryService;

    // 조회
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories(Authentication authentication) {
        List<CategoryResponse> categories = categoryService.getCategories(authentication.getName());
        return ResponseEntity.ok(categories);
    }

    // 추가
    @PostMapping
    public ResponseEntity<Void> createCategory(Authentication authentication, @Valid @RequestBody CategoryRequest request) {
        categoryService.createCategory(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 수정
    @PutMapping("/{categoryId}")
    public ResponseEntity<Void> updateCategory(Authentication authentication, @PathVariable Long categoryId, @Valid @RequestBody CategoryRequest request) {
        categoryService.updateCategory(authentication.getName(), categoryId, request);
        return ResponseEntity.ok().build();
    }

    // 삭제 (soft delete)
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(Authentication authentication, @PathVariable Long categoryId) {
        categoryService.deleteCategory(authentication.getName(), categoryId);
        return ResponseEntity.noContent().build();
    }

}
