package com.server.dosopt.seminar.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
@AllArgsConstructor: 클래스의 모든 field를 포함하는 생성자 생성 - 각 field에 대한 초기화를 처리
-> 열거형 상수를 초기화하기 간편해짐
ex. Part server = new Part("server"); -> Part server = Part.SERVER;
 */
@Getter
@AllArgsConstructor
public enum Part {
    SERVER("server"),
    WEB("web"),
    ANDROID("android"),
    IOS("ios"),
    PLAN("plan"),
    DESIGN("design");

    private final String name;
}
