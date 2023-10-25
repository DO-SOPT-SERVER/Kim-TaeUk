package com.server.dosopt.seminar.controller;

import com.server.dosopt.seminar.dto.MemberCreateRequest;
import com.server.dosopt.seminar.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController // Spring에서 RESTful 컨트롤러임을 정의
@RequestMapping(MemberController.MEMBER_BASE_URL)  // 공통된 url mapping
@RequiredArgsConstructor
public class MemberController {

    /*
    @RequiredArgsConstructor: final이나 @NonNull이 붙은 field에 대해 자동으로 생성자 생성
    -> DI를 위해 주로 사용!

    이유:
    Spring에서 DI를 위해 Constructor Injection을 사용,
    private final MemberService memberService;가 어노테이션을 사용함으로써
    MemberService 객체를 MemberController 클래스에 편리하게 주입할 수 있음
     */
    private final MemberService memberService;
    public static final String MEMBER_BASE_URL = "api/member/";

    // Create
    /*
    RESTful API에서 새로운 resource 생성 시퀀스

    1. 클라이언트에서 POST request 보냄
    요청에는 json을 태워서 보냄, 아래 예시 처럼
    {
        "name": "김태욱",
        "nickname": "wookionnnon",
        "age": 25,
        "sopt": {
            "generation": 33,
            "part": "SERVER"
        }
    }

    2. request가 서버에 도착 -> Spring MVC는 @RequestBody를 사용,
    해당 json 데이터를 MemberCreateRequest 객체로 mapping함.
    -> json의 key와 MemberCreateRequest 객체의 field가 일치하면 그 값을 객체 field에 할당함

    3. MemberController의 createMember 메서드가 호출
    -> @RequestBody로 mapping된 MemberCreateRequest 객체를 받아옴

    4. memberService.create(request) 호출
    -> 새로운 member 생성(builder 패턴 사용)하고, DB에 저장(memberJpaRepository.save(member);)

    5. DB에 저장된 새로운 member의 id(memberId)를 가져옴(return savedMember.getId().toString();)

    6. MEMBER_BASE_URL과 memberId concat해서 새로운 URI 생성
    -> 새로운 URI: 새로 생성된 member의 위치(클라이언트에서는 이 URI로 member의 정보 가져올 수 있음)

    7. ResponseEntity.created(location)을 사용,
    클라이언트에게 201Created와 새로운 member의 URI가 포함된 response를 반환
    -> 클라이언트에게 새로운 resource가 생김을 알림
     */
    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody MemberCreateRequest request) {
        String memberId = memberService.create(request);
        URI location = URI.create(MEMBER_BASE_URL + memberId);
        return ResponseEntity.created(location).build();
    }
}

