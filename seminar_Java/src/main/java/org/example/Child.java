package org.example;

public class Child extends Parents{
    @Override
    public void hello() {
        super.hello();
        // Child는 Parents를 상속받음, 따로 구현하지 않아도 슈퍼클래스의 기능 사용 가능
    }
}
