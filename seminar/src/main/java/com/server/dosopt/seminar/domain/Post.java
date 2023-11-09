package com.server.dosopt.seminar.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
1.
@Entity(name = ""): entity의 이름 설정
-> HQL에서 엔티티를 식별할 이름

2.
@Table: entity와 mapping할 테이블 지정
@Table(name = ""): DB에 생성될 mapping table의 이름 지정

3.
@Entity(name = "")만 사용: Entity + Table 기능 수행
-> @Entity(name = "")에서 지정한 이름으로 entity와 table 이름 지정

@Entity와 @Table 따로 사용: 각자 설정한 값에 따라 작동
-> entity 이름은 @Entity(name = "")에서 지정한 이름,
table 이름은 @Table(name = "")에서 지정한 이름으로!
*/

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    /*
    JPA에서 @Entity 클래스 안의 컬럼명을 Camel Case(postId)로 써도
    테이블 생성 시 DB에는 Snake Case(post_id)로 들어감
    -> hibernate의 default 옵션이 그렇기 때문에

    application.properties에서 옵션 설정해서 Entity에서 설정한 컬럼명 그대로 넣을 수도 있음
    -> 추가: spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl

    //Post - Member 연관관계에서 주인은 Post임!
    //일대다에서는 다가 주인이 됨!
     */

    private String title;

    /*
    @Column 어노테이션의 columnDefinition 속성
    : Entity 클래스의 field가 DB table에서 사용될 때 데이터 유형 및 제약 조건 정의
    -> TEXT 데이터 유형 사용을 지정
     */
    @Column(columnDefinition = "TEXT")
    private String content;

    /*
    @ManyToOne
    Post(게시글) : Member(사용자) => 다대일 관계
    : 사용자 1명은 여러 개의 게시글을 작성할 수 있기 때문
    여러 개의 게시글이 사용자 한 명에게 사용될 수 있으니까

    @JoinColumn
    : FK mapping 시 사용

    name 속성 - mapping할 FK 이름 지정
    -> 지정한 이름 가지는 FK column 생성됨
    지정X -> default: field명_참조핱테이블PK명
    */

    /*
    FetcyType.EAGER(즉시 로딩) VS FetcyType.LAZY(지연 로딩)

    FetcyType.EAGER
    : entity 조회 시 연관된 Entity도 함께 조회
    - 연관 entity가 필요 없어도 함께 로드됨

    FetcyType.LAZY
    : entity를 실제 사용할 때만 조회
    - 연관 entity가 필요할 시점에 쿼리 실행하여 로드
    -> JPA 구현체는 실제 entity 대신 proxy 객체를 반환
     */
    @ManyToOne(fetch = FetchType.LAZY)  // @ManyToOne은 default가 EAGER임
    @JoinColumn(name = "member_id")
    private Member member;  // 실제로 mapping할 object를 걸어줘야 함

    @Builder
    public Post(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
