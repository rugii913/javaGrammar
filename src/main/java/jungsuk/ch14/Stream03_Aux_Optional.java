package jungsuk.ch14;

import java.time.LocalTime;
import java.util.Optional;
import java.util.OptionalInt;

// ch.14 p.835 Optional<T> 및 기본 타입 Optional 관련
// cf. java.util.Optional은 JDK 1.8부터
public class Stream03_Aux_Optional {
    // stream의 최종 연산의 결과 타입이 Optional
    // -> 반환 결과가 null인지 if로 체크하는 대신 Optional에 정의된 메서드를 통해 간단히 처리 가능
    // cf. Objects 클래스에 isNull(), nonNull(), requireNonNull()과 같은 메서드가 있는 것도 null check를 위한 if를 메서드 안으로 넣어 코드의 복잡도를 낮추기 위한 것

    // *** p.835 ~ 839 내용 + p.839 예제 14-12 관련 내용을 아래 세 메서드로 나누어 놓았음 ***

    // Optional 생성
    public void optional_creation() {
        System.out.println("optional_creation()");

        Optional<String> optionalStr1 = Optional.of("abc");
        String str = "abcde";
        Optional<String> optionalStr2 = Optional.of(str);

        // 참조변수의 값이 null일 가능성이 있으면, of() 대신 ofNullable()
        // cf. null이 확실하면 empty()를 사용해야 한다.
        String str2 = LocalTime.now().getSecond() % 2 == 0 ? "not null, it is String" : null;
        Optional<String> optionalStr3 = Optional.ofNullable(str2);

        Optional<String> optionalStr4 = Optional.empty();
        // Optional<String> optionalStr5 = null; // 이건 좀...

        System.out.println("============================================================");
    }

    // Optional의 값
    public void optional_value() {
        System.out.println("optional_value()");
        // 값을 가져올 때 기본적으로는 get() 사용 - value 필드가 null인 경우 NoSuchElementException 발생
        Optional<String> optionalStr1 = Optional.ofNullable(LocalTime.now().getSecond() % 2 == 0 ? "not null, it is String" : null);
        // String string = optionalStr.get(); // value가 null이면 예외 터짐
        optionalStr1.ifPresent(s -> System.out.println("optionalStr value = " + s)); // ifPresent(consumer) 람다식 이용 - 아래에서 다시 사용
        System.out.println("optionalStr.orElse(\"value of this optional was null\") = " + optionalStr1.orElse("value of this optional was null"));
        // orElseGet(supplier), orElseThrow(exceptionSupplier)
        String orElseGetEx = optionalStr1.orElseGet(() -> LocalTime.now().getSecond() % 2 == 0 ? "second is even" : "second is odd");
        // String orElseThrowEx = optionalStr.orElseThrow(RuntimeException::new); // value가 null인 경우 지정된 예외로 변환

        System.out.println("------------------------------------------------------------");
        // Optional의 filter(), map(), flatMap()
        Optional<String> optionalStr2 = Optional.of(LocalTime.now().getSecond() % 2 == 0 ? "123" : "-123");

        Optional<Integer> optionalInt = optionalStr2.map(String::length);
        System.out.println("optionalInt value = " + optionalInt.get());

        int result1 = optionalStr2.filter(x -> x.length() > 0).map(Integer::parseInt).get();
        System.out.println("result1 = " + result1);

        // 만약 Optional value가 null이면, Optional의 filter(~), map(~), flatMap(~) 등의 메서드는 아무 일도 하지 않는다.
        // - 아래 코드의 map(~)도 아무 일도 하지 않음
        int result2 = Optional.of("").filter(x -> x.length() > 0).map(Integer::parseInt).orElse(-1);
        System.out.println("result2 = " + result2);

        System.out.println("------------------------------------------------------------");
        // ifPresent(~): parseInt(~)처럼 예외가 발생하기 쉬운 메서드를 다룰 때, 예외 처리 대신 ifPresent(~)를 사용
        // optional의 value가 있으면, 그 value를 받아서 consumer 람다식을 실행, value가 null이면 아무 것도 하지 않음
        String nullableIntString = LocalTime.now().getSecond() % 2 == 0 ? "456" : null;
        try {
            int result3 = Optional.ofNullable(nullableIntString).map(Integer::parseInt).get();
            System.out.println(result3);
        } catch (Exception e) {
            // some exception handling logic
        }
        boolean isNullableIntStringPresent = Optional.ofNullable(nullableIntString).isPresent();
        System.out.println("isNullableIntStringPresent = " + isNullableIntStringPresent);
        // 위처럼 try catch 방식이 아니라 ifPresent(~)로 간단하게 처리 - 주로 stream의 findAny(), findFirst()와 같은 메서드들과 함께 사용한다.
        Optional.ofNullable(nullableIntString).map(Integer::parseInt).ifPresent(x -> System.out.printf("result3 = %d%n", x));
        // cf.Stream 클래스에 정의된 메서드 중 반환타입이 Optional<T>인 것은
        // finaAny(), findFirst(), max(~), min(~), reduce(~)

        System.out.println("------------------------------------------------------------");
        // optional_optionalIntLongDouble()의 OptionalInt.of(0) vs. OptionalInt.empty()와 비교
        Optional<String> optional1 = Optional.ofNullable(null); // null을 저장 (IDE 경고) 'Optional.ofNullable()' with null argument should be replaced with 'Optional.empty()'
        System.out.println("optional1 = " + optional1);
        Optional<String> optional2 = Optional.empty(); // 빈 객체를 생성
        System.out.println("optional2 = " + optional2);
        System.out.println("optional1.equals(optional2) ? " + optional1.equals(optional2)); // true;

        System.out.println("============================================================");
    }

    // 기본 타입 스트림으로 인한 기본 타입 Optional
    public void optional_optionalIntLongDouble() {
        System.out.println("optional_optionalIntLongDouble()");
        // IntStream, ...과 같은 기본 타입 스트립에서는 value가 기본형인 OptionalInt, ... 등을 반환한다.
        // findAny(), findFirst(), reduce(~), max(), min() 등의 메서드는 비슷하고, average() 메서드가 추가로 있음
        // value를 꺼낼 때 사용하는 메서드 이름이 get()이 아니라 getAsInt(), getAsLong(), getAsDouble()이다.
        OptionalInt optionalInt1 = OptionalInt.of(0); // 0을 저장
        System.out.println(optionalInt1.isPresent()); // true
        OptionalInt optionalInt2 = OptionalInt.empty(); // 빈 객체를 생성 // 이 객체의 경우에도 value = 0일 것이다. 그러나 isPresent 필드를 통해 구분할 수 있다.
        System.out.println(optionalInt2.isPresent()); // false

        // optional_value()의 Optional.ofNullable(null) vs. Optional.empty()와 비교
        System.out.println(optionalInt1.getAsInt()); // 0
        System.out.println("optionalInt1 = " + optionalInt1);
        // System.out.println(optionalInt2.getAsInt()); // NoSuchElementException - if no value is present
        optionalInt2.ifPresentOrElse(System.out::println, () -> System.out.println("this is empty OptionalInt"));
        System.out.println("optionalInt2 = " + optionalInt2);
        System.out.println("optionalInt1.equals(optionalInt2) ? " + optionalInt1.equals(optionalInt2));

        System.out.println("------------------------------------------------------------");
        int result1 = optionalStringToInt(Optional.of("123"), 0);
        int result2 = optionalStringToInt(Optional.of(""), 0);

        System.out.println("result1 = " + result1);
        System.out.println("result2 = " + result2);

        System.out.println("============================================================");
    }

    private int optionalStringToInt(Optional<String> optionalString, int defaultValue) { // 별로 안 좋은 방법인 거 같은데...
        try {
            return optionalString.map(Integer::parseInt).get();
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
