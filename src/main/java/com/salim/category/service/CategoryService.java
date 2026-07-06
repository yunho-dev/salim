package com.salim.category.service;

import com.salim.category.dto.CategoryRequest;
import com.salim.category.dto.CategoryResponse;
import com.salim.category.entity.Category;
import com.salim.category.repository.CategoryRepository;
import com.salim.member.entity.Member;
import com.salim.member.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    // 조회
    public List<CategoryResponse> getCategories(String memberId) {
        Member member = getMember(memberId);
        return categoryRepository.findByMemberAndIsDeletedFalseOrderByInsertDateDesc(member)
                .stream()
                .map(CategoryResponse::from)
                .toList();
    }

    // 추가
    @Transactional
    public void createCategory(String memberId, @Valid CategoryRequest request) {
        Member member = getMember(memberId);

        Category category = Category.builder()
                .member(member)
                .categoryType(request.categoryType())
                .categoryName(request.categoryName())
                .iconKey(request.iconKey())
                .build();

        categoryRepository.save(category);
    }

    // 수정
    @Transactional
    public void updateCategory(String memberId, Long categoryId, @Valid CategoryRequest request) {
        Category category = getOwnedCategory(memberId, categoryId);
        category.update(request.categoryName(), request.categoryType(), request.iconKey());
    }

    // 삭제 (soft delete)
    @Transactional
    public void deleteCategory(String memberId, Long categoryId) {
        Category category = getOwnedCategory(memberId, categoryId);
        category.delete();
    }

    private Member getMember(String memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    // 본인 소유 카테고리인지 함께 확인 (타인의 categoryId로 접근하는 것을 방지)
    private Category getOwnedCategory(String memberId, Long categoryId) {
        Member member = getMember(memberId);
        return categoryRepository.findByCategoryIdAndMemberAndIsDeletedFalse(categoryId, member)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
    }
}
