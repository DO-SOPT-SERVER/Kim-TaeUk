package com.server.dosopt.seminar.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
@Entity: Member 클래스가 JPA entity 임을 나타냄.
-> JPA가 해당 entity를 DB table과 mapping함

@Getter: getter 메서드 자동 생성(lombok)
-> 객체의 field 읽을 수 있음

@NoArgsConstructor: 매개변수가 없는 생성자 생성, 접근 수준은 PROTECTED로 설정
-> PUBLIC이나 PROTECTED로 설정함(Proxy와 관련 있음)
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    /*
    @Id: 'id' field가 entity의 PK임을 나타냄

    @GeneratedValue: PK값에 대한 생성 전략을 제공함, 생성 전략은 IDENTITY
    -> PK 생성을 db에 위임, 주로 RDB에서 사용하며 auto-increment 컬럼을 활용하여 PK를 생성함
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nickname;
    private int age;

    /*
    @Embedded: JPA entity 안의 컬럼을 하나의 객체로 사용하고 싶을 때 사용
    - 값 타입을 사용하는 곳에 표시
    -> Member 객체에서 embedded 타입으로 된 SOPT 객체를 사용!
     */
    @Embedded
    private SOPT sopt;

    /*
    @Builder: Builder 패턴 사용하여 객체 생성하게 해줌(lombok)
     */
    @Builder
    public Member(String name, String nickname, int age, SOPT sopt) {
        this.name = name;
        this.nickname = nickname;
        this.age = age;
        this.sopt = sopt;
    }

    public void updateSOPT(SOPT sopt) {
        this.sopt = sopt;
    }
}