package org.example;

public class Cat implements Animal {
    //Animal 인터페이스를 통한 구현체 - method의 구현부
    @Override
    public void cry() {
        System.out.println("meow");
    }
}
