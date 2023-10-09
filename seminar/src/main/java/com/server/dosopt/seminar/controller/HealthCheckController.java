package com.server.dosopt.seminar.controller;

import com.server.dosopt.seminar.dto.HealthCheckResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")  // 공통된 url - class level에서 작성
public class HealthCheckController {

    @GetMapping("/v1")  // 추가 url 작성 -> /health/v1
    public Map<String, String> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");

        return response;    // json 아닌 map 형태로 반환 -> Map<String, String>
    }

    @GetMapping("/v2")
    public ResponseEntity<String> healthCheckV2() {
        return ResponseEntity.ok("OK"); // ResponseEntity 사용하여 ResponseEntity<String> 반환(json 아닌 문자열)
    }

    @GetMapping("/v3")
    public String healthCheckV3() {
        return "OK";    // 단순 문자열 반환(json 아닌 문자열)
    }

    @GetMapping("/v4")
    public ResponseEntity<Map<String, String>> healthCheckV4() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");

        // ResponseEntity 사용하여 ResponseEntity<Map<String, String>> 반환(json 형태의 map)
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v5")
    public ResponseEntity<HealthCheckResponse> healthCheckV5() {
        // HealthCheckResponse 객체 생성해서 ResponseEntity<HealthCheckResponse> 반환
        return ResponseEntity.ok(new HealthCheckResponse());
    }

    /* 406 error 발생 -> Serialization에서 문제 -> HealthCheckResponse에서 @Getter로 해결
    Postman:
    {
        "timestamp": "2023-10-08T18:19:48.263+00:00",
            "status": 406,
            "error": "Not Acceptable",
            "path": "/health/v5"
    }

    log:
    org.springframework.web.HttpMediaTypeNotAcceptableException: No acceptable representation
    */


}

