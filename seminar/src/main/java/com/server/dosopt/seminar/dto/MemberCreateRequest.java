package com.server.dosopt.seminar.dto;

import com.server.dosopt.seminar.domain.SOPT;

/*
MemberCreateRequest: Member entity 생성하기 위한 request 데이터를 나타내기 위한 DTO
-> Record 클래스로 DTO 정의

Record 특징
1. 생성자와 멤버 변수 자동 생성 - 생성자 매개변수는 레코드의 필드가 됨
2. 생성자 배개변수와 동일한 이름의 멤버 변수 생성, 자동으로 private & final
3. getter, equals, hashcode, toString 자동 생성됨
 */
public record MemberCreateRequest(
        String name,
        String nickname,
        int age,
        SOPT sopt
) {
}
