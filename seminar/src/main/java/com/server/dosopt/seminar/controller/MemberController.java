package com.server.dosopt.seminar.controller;

import com.server.dosopt.seminar.dto.MemberCreateRequest;
import com.server.dosopt.seminar.dto.MemberGetResponse;
import com.server.dosopt.seminar.dto.MemberProfileUpdateRequest;
import com.server.dosopt.seminar.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController // Spring에서 RESTful 컨트롤러임을 정의
@RequestMapping("/api/member")  // 공통된 url mapping
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
    public static final String MEMBER_BASE_URL = "/api/member/";

    // 생성
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

    // 특정 사용자 정보 단건 조회 V1
    @GetMapping("/{memberId}/v1")
    public ResponseEntity<MemberGetResponse> getMemberProfileV1(@PathVariable Long memberId) {
        /*
        @PathVariable: 경로 변수 가져오는 데 사용
        -> URI에서 정의한 경로 변수 {memberId}를 메서드의 매개변수로 받음!
         */
        MemberGetResponse response = memberService.getMemberByIdV2(memberId);
        return ResponseEntity.ok(response);
    }

    // 특정 사용자 정보 단건 조회 V2
    /*
    @GetMapping 속성 - consumes, produces
    -> 사용함으로써 API endpoint의 req/res 형식을 명확히 함, client-server간 통신 안전하게 함

    consumes
    : 클라이언트가 보내는 request의 Content-Type 헤더를 제한함
    consumes = "application/json" -> json 형식으로 보낼 때만 해당 endpoint 호출 가능

    produces
    : 서버가 생성하는 response의 Content-Type의 헤더를 제한함
    produces = MediaType.APPLICATION_JSON_VALUE -> json 형식의 response를 클라이언트에게 보낼 것을 명시함
     */
    @GetMapping(value = "/{memberId}/v2", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberGetResponse> getMemberProfileV2(@PathVariable Long memberId) {
        MemberGetResponse response = memberService.getMemberByIdV2(memberId);
        return ResponseEntity.ok(response);
    }

    // 전체 사용자 조회
    @GetMapping
    public ResponseEntity<List<MemberGetResponse>> getMembersProfile() {
        List<MemberGetResponse> memberGetResponseList = memberService.getMembers();
        return ResponseEntity.ok(memberGetResponseList);
    }

    // 삭제
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        /*
        삭제 요청이 성공적으로 처리
        -> 클라이언트에게 204 No Content response를 반환
         */
        return ResponseEntity.noContent().build();
    }

    // 수정
    @PatchMapping("/{memberId}")
    public ResponseEntity<Void> updateMemberSoptInfo(@PathVariable Long memberId,
                                                     @RequestBody MemberProfileUpdateRequest request) {
        memberService.updateSOPT(memberId, request);
        return ResponseEntity.noContent().build();
    }
}

