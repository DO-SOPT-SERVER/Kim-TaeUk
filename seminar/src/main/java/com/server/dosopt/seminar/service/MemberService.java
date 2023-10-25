package com.server.dosopt.seminar.service;

import com.server.dosopt.seminar.domain.Member;
import com.server.dosopt.seminar.dto.MemberCreateRequest;
import com.server.dosopt.seminar.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
@Service: Service 계층 컴포넌트로 선언,
SpringApplicationContext에 service bean으로 등록함!
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    /*
    @Transactional: Spring에서 선언적 트랜잭션 처리를 지원!

    readOnly = false(default)
    -> 읽기와 쓰기 모두 수행, 데이터 변경 포함한 작업 수행 시 필요

    readOnly = true
    -> 읽기만 수행, 데이터 수정하지 않는 경우에 사용
     */

    private final MemberJpaRepository memberJpaRepository;

    @Transactional
    public String create(MemberCreateRequest request) {
        Member member = Member.builder()
                .name(request.name())
                .nickname(request.nickname())
                .age(request.age())
                .sopt(request.sopt())
                .build();

        Member savedMember = memberJpaRepository.save(member);
        return savedMember.getId().toString();
    }
}

