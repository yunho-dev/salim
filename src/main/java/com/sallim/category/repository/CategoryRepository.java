package com.sallim.category.repository;

import com.sallim.category.entity.Category;
import com.sallim.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByMemberAndIsDeletedFalseOrderByInsertDateDesc(Member member);

    Optional<Category> findByCategoryIdAndMemberAndIsDeletedFalse(Long categoryId, Member member);

}
