package com.salim.category.entity;

import com.salim.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "CATEGORY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", nullable = false, length = 10)
    private CategoryType categoryType;

    @Column(name = "category_name", nullable = false, length = 300)
    private String categoryName;

    @Column(name = "icon_key", nullable = false, length = 50)
    private String iconKey;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "insert_date", nullable = false, updatable = false)
    private LocalDateTime insertDate;

    @UpdateTimestamp
    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;

    @Builder
    public Category(Member member, CategoryType categoryType, String categoryName, String iconKey) {
        this.member = member;
        this.categoryType = categoryType;
        this.categoryName = categoryName;
        this.iconKey = iconKey;
    }

    public void update(String categoryName, CategoryType categoryType, String iconKey) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
        this.iconKey = iconKey;
    }

    public void delete() {
        this.isDeleted = true;
    }

}
