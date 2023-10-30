package com.world.alfs.domain.member;

import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String identifier;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int birth;

    @Column(nullable = false)
    private int point;

    @Column(nullable = false)
    private String email;

    @Column(name = "phone_number",nullable = false)
    private int phoneNumber;

    @Builder
    public Member(Long id, String name, String identifier, String password, int birth, int point, String email, int phoneNumber) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
        this.password = password;
        this.birth = birth;
        this.point = point;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
