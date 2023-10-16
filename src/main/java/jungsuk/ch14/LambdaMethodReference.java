package jungsuk.ch14;

import java.math.BigInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

// ch.14 p.812 메서드 참조
public class LambdaMethodReference {

    /*
    * method reference
    * - 람다식이 하나의 메서드만 호출 & functional interface의 generic type 및 참조하는 method signature로부터 추론 가능한 정보가 있는 경우
    * - 람다식이 들어갈 자리에 메서드 참조를 넣을 수 있다.
    *
    * */

    public void methodReference() {
        System.out.println("methodReference()");

        Function<String, Integer> f1 = Integer::parseInt; // static method 참조
        // Function<String, Integer> f1 = str -> Integer.parseInt(str);
        int a = f1.apply("35");
        System.out.printf("a = %d\n", a);

        BiFunction<String, String, Boolean> f2 = String::equals; // instance method 참조 - method를 호출하는 instance까지, 람다식을 호출할 때 인자로 받을 경우
        // BiFunction<String, String, Boolean> f2 = (s1, s2) -> s1.equals(s2);
        String s1 = "abc";
        String s2 = "abc";
        boolean b1 = f2.apply(s1, s2);
        System.out.printf("b1 = %b\n", b1);

        String string1 = "abc"; // 특정 객체의 instance method 참조 - method를 호출하는 instance는 이미 지정되어 있는 경우
        Function<String, Boolean> f3 = string1::equals;
        // Function<String, Boolean> f3 = str -> string1.equals(str);
        String string2 = "abcd";
        boolean b2 = f3.apply(string2);
        System.out.printf("b2 = %b\n", b2);

        System.out.println("============================================================");
    }

    public void constructorReference() {
        System.out.println("constructorReference()");

        // new 참조 - 생성자를 호출하는 람다식을 메서드 참조로 사용하는 방법 - 매개변수가 있는 생성자라면, 매개변수의 개수에 따라 적절한 functional interface를 사용하면 된다.
        // - 필요한 경우 적절한 functional interface를 새로 정의하면 된다.
        Supplier<String> s = String::new;
        Function<String, CharSequence> f = StringBuilder::new;
        BiFunction<String, Integer, BigInteger> b = BigInteger::new;
        
        // 배열로 이러한 방식으로 생성할 수 있다.
        Function<Integer, int[]> fInt = int[]::new;
        // Function<Integer, int[]> fInt = x -> new int[x];

        System.out.println("============================================================");
    }
}
