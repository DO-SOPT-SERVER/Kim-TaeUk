package com.server.dosopt.seminar.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

/*
@MappedSuperclass
: 엔티티 클래스 간 코드 재사용과 공통 매핑 정보 공유를 위한 역할을 하는 추상 클래스 정의 시 사용

JPA에서 사용
DB 테이블과 직접 mapping X!
하위 엔티티 클래스에서는 상속 -> 공통 필드와 매핑 정보 상속!

ex. Member extends BaseTimeEntity
-> Member 테이블에 createdDate, updatedDate 컬럼 포함!
 */

/*
@EntityListeners(AuditingEntityListener.class)
: 클래스에 Auditing 기능 부여
-> 엔티티 객체의 라이프 사이클(생성, 수정, 삭제 등) 이벤트 발생 시, 이벤트 감지하고 특정 동작 수행하게 함
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    /*
    @CreatedDate
    : Entity 생성되어 저장될 때 시간 자동 저장

    @LastModifiedDate
    : 조회한 Entity의 값 변경할 때 시간 자동 저장
     */
    @CreatedDate
    private LocalDate createdDate;

    @LastModifiedDate
    private LocalDate updatedDate;
}

