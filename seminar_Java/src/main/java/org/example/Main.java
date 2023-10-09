package org.example;

public class Main {
    public static void main(String[] args) {

        Child child = new Child();
        child.hello();

//        Poketmon pikachu = new Poketmon("pikachu", PoketmonType.ELECTRIC);

//        field(name, type)에 직접적으로 접근X -> private + getter 사용
//        System.out.println(pikachu.name);
//        System.out.println(pikachu.type);

        AquaPoketmon aquqaPoketmon = new AquaPoketmon("kkobugi");
    }

}