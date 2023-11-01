package com.server.dosopt.seminar.repository;

import com.server.dosopt.seminar.domain.Member;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

/*
MemberJpaRepository: Spring Data JPA에서 제공하는 JpaRepository(슈퍼 클래스)를 상속받음
JpaRepository: interface인데, MemberJpaRepository에게
save(entity), delete(entity), findById(id), findAll() 메서드를 상속해줌
-> override해서 사용할 수도 있음~
 */
public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    /*
    findByIdOrThrow: id 찾으면 반환, 찾지 못하면 EntityNotFoundException 던지는 커스텀 메서드

    default 키워드 사용함으로써 MemberJpaRepository의 구현체에서는 override하지 않아도 사용 가능함
     */
    default Member findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 회원입니다."));
    }
}