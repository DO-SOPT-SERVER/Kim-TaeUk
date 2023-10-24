package com.server.dosopt.seminar.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;

/*
@Embeddable: JPA entity 안의 컬럼을 하나의 객체로 사용하고 싶을 때 사용
- 값 타입을 정의하는 곳에 표시
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SOPT {
    private int generation;

    /*
    Enumerated 어노테이션 - 2가지 mapping 값 존재
    1. EnumType.ORDINAL
    : enum의 순서를 db에 저장

    2. EnumType.STRING
    : enum의 이름 자체가 db에 저장
     */
    @Enumerated(value = STRING)
    private Part part;
}
