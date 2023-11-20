package com.server.dosopt.seminar.dto.member;

import com.server.dosopt.seminar.domain.Member;
import com.server.dosopt.seminar.domain.SOPT;

public record MemberGetResponse(
        String name,
        String nickname,
        int age,
        SOPT soptInfo
) {
    /*
    팩토리 메서드 패턴 적용(- of 정적 메서드)
    객체 생성 추상화, 클라이언트 코드로부터 숨기는 디자인 패턴
    -> 객체 생성 로직 캡슐화, 객체 생성 시 유연성 제공

    정적 메서드(static method): 클래스로 객체를 생성하지 않아도 호출 가능한 메서드
     */
    public static MemberGetResponse of(Member member) {
        return new MemberGetResponse(
                member.getName(),
                member.getNickname(),
                member.getAge(),
                member.getSopt()
        );
    }
}
