package com.server.dosopt.seminar.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
@RestControllerAdvice = @ControllerAdvice + @ResponseBody
: 전역적으로 예외 처리할 수 있는 어노테이션, 응답을 json으로 내려줌(@ResponseBody로 인해)

@ExceptionHandler
    : AOP 이용한 예외처리 방식,
    메서드에 선언하여 예외 처리하려는 클래스 지정 -> 예외 발생 시 정의된 로직에 의해 처리!
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /*
    controller에서 발생하는 BusinessException.class 및 서브 클래스에 속한 예외 발생 시
    -> HttpStatus와 함께 메시지를 처리할 수 있음
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}