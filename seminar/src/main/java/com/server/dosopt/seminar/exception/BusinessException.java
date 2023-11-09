package com.server.dosopt.seminar.exception;

/*
사용자 정의 예외 클래스로 BusinessException 클래스를 정의하고 있음
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}

