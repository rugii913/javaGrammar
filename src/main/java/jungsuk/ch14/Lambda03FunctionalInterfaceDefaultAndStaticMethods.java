package jungsuk.ch14;

import java.util.function.Function;
import java.util.function.Predicate;

// ch.14 p.808 Function의 합성과 Predicate의 결합
public class Lambda03FunctionalInterfaceDefaultAndStaticMethods {

    // p.808 ~ 809 및 p.811 예제 14-7 관련
    public void functionDefaultMethods_AndThen_Compose() {
        System.out.println("functionDefaultMethods_andThen_compose()");

        Function<String, Integer> f = s -> Integer.parseInt(s, 16); // 주어진 String을 16진수 int로 캐스팅
        Function<Integer, String> g = Integer::toBinaryString; // 주어진 int를 binary 숫자 모양 String으로 캐스팅
        // p.812 단 하나의 메서드만 호출하며, generic type 등으로 람다식의 매개 변수를 추론할 수 있는 경우 메서드 참조를 통해 생략 가능
        // Function<Integer, String> g = i -> Integer.toBinaryString(i);

        // default <V> Function<V, R> compose(Function<? super V, ? extends T> before)
        // 매개변수: before – the function to apply before this function is applied
        // return (V v) -> apply(before.apply(v)); 방식으로 구현
        Function<String, String> h1 = f.andThen(g); // 왼쪽에서 오른쪽으로 읽어나가는 순서처럼 f 결과를 g로 넣어서 연산

        // default <V> Function<T, V> andThen(Function<? super R, ? extends V> after)
        // 매개변수: after – the function to apply after this function is applied
        // return (T t) -> after.apply(apply(t));
        Function<String, String> h2 = g.compose(f); // 수학 합성함수처럼 g(f()) 형태로 f 결과를 g로 넣어서 연산

        System.out.println(h1.apply("FF")); // "FF" -> 255 -> "11111111"
        System.out.println(h2.apply("FF")); // "FF" -> 255 -> "11111111"

        System.out.println("============================================================");
    }

    // p.810 및 p.811 예제 14-7 관련
    public void functionStaticMethod_identity() {
        System.out.println("functionStaticMethod_identity()");

        Function<String, String> f = Function.identity(); // 항등함수(identity function)
        // Function<String, String> f = x -> x;
        System.out.println(f.apply("AAA")); // AAA가 그대로 출력
        // 보통 stream에서 map()으로 변환 작업할 때, 변환 없이 그대로 처리하기 위해 사용

        System.out.println("============================================================");
    }

    // p.810 및 p.811 예제 14-7 관련
    public void predicateDefaultMethods_and_or_nagate() {
        System.out.println("predicateDefaultMethods_and_or_nagate()");

        Predicate<Integer> p = i -> i < 100;
        Predicate<Integer> q = i -> i < 200;
        Predicate<Integer> r = i -> i % 2 == 0;
        Predicate<Integer> notP = p.negate(); // !(i < 100)

        Predicate<Integer> s = notP.and(q.or(r));
        System.out.println(s.test(150)); // 150은 notP이고, q, r이므로 s

        System.out.println("============================================================");
    }

    public void predicateStaticMethod_isEqual() {
        System.out.println("predicateStaticMethod_isEqual()");

        String targetRef = "abc";
        String str1 = "abc";
        String str2 = "abcd";

        // p2의 추상 메서드 test()가 isEqual(~) static 메서드로 구현된다.
        Predicate<String> p = Predicate.isEqual(targetRef);
//        static <T> Predicate<T> isEqual(Object targetRef) {
//            return (null == targetRef) ? Objects::isNull : object -> targetRef.equals(object);
//        }
        // isEqual(~)의 인자로 들어간 값과 비교하는 식이 된다. // isEqual(~)의 인자로 null이 들어가면, null인지 아닌지를 판별함
        boolean result1 = p.test(str1);
        boolean result2 = p.test(str2);
        System.out.printf("p.test(str1) = %b \n", result1);
        System.out.printf("p.test(str2) = %b \n", result2);

        System.out.println("============================================================");
    }
}
