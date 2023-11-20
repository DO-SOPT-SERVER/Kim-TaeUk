package com.server.dosopt.seminar.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
    /*
    ServerSeminarApplication에서 분리하여 관리
    -> Mock 테스트 시 문제 발생할 수 있음
    : @WebMvcTest 테스트 시 'JPA metamodel must not be empty!' 에러가 발생한다고 함

    이유
    : @WebMvcTest는 JPA 생성 관련 기능이 전혀 존재하지 않는 테스트 어노테이션임
    테스트는 Application이 돌면서 작동함
    그런데 @EnableJpaAuditing을 Application 위에 올리면 JPA 관련 bean들이 올라오는 것을 요구함
    이때, mockMvc를 사용한 테스트는 mvc와 관련한 bean만 찾아 올리므로 JPA 관련 bean을 찾지 못하기 때문
    이러한 이유때문에 지금처럼 config 클래스를 생성하여 분리하거나,
    테스트 코드 클래스마다 @MockBean(JpaMetamodelMappingContext.class)를 추가하여 해결한다고 함
     */
}
