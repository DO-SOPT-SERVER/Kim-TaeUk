package org.example;

public abstract class Poketmon {
    // 추상 클래스로 만들고, 상속해서 기존 기능 활용 + 확장
    private String name;
    private PoketmonType type;

    public Poketmon(String name, PoketmonType type) {
        this.name = name;
        this.type = type;
    }
}
