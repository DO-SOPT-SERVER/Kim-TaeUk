package com.server.dosopt.seminar.service;

import com.server.dosopt.seminar.domain.Member;
import com.server.dosopt.seminar.dto.MemberCreateRequest;
import com.server.dosopt.seminar.dto.MemberGetResponse;
import com.server.dosopt.seminar.repository.MemberJpaRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public MemberGetResponse getMemberByIdV1(Long id) {
        /*
        memberJpaRepository.findById(id)의 반환값은 Optional<Member>임.
        -> Member 객체가 있을 수도 있고 비어있을 수도 있음

        id에 해당하는 Member 객체가 없는 경우 NoSuchElementException 발생함
        -> isPresent()나 ifPresent()를 통해 확인 후 호출하고, 비어있을 경우 예외처리하는 것이 올바름
         */
        Member member = memberJpaRepository.findById(id).get();
        return MemberGetResponse.of(member);
    }

    /*
    getMemberByIdV2와 getMemberByIdV3는 기능적으로 같음
    하드코딩한 에러 메시지와 함께 EntityNotFoundException를 직접 던지기 -> getMemberByIdV2
    findByIdOrThrow 메서드 사용해서 예외 처리 추상화 -> getMemberByIdV3
     */
    public MemberGetResponse getMemberByIdV2(Long id) {
        Member member = memberJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        return MemberGetResponse.of(member);
    }

    public MemberGetResponse getMemberByIdV3(Long id) {
        Member member = memberJpaRepository.findByIdOrThrow(id);
        return MemberGetResponse.of(member);
    }
}

