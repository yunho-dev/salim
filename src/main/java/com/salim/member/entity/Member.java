package com.salim.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "MEMBER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name = "member_id", nullable = false, length = 40)
    private String memberId;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime insertDate;

    @UpdateTimestamp
    @Column(insertable = false)
    private LocalDateTime updateDate;

    @Builder
    private Member(String memberId, String password, String email, String name) {
        this.memberId = memberId;
        this.password = password;
        this.email = email;
        this.name = name;
    }
}