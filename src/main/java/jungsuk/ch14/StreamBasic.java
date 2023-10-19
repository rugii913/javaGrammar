package jungsuk.ch14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

// ch.14 p.814 스트림
public class StreamBasic {
    /*
    * 스트림 (cf. JDK 1.8부터 lambda expression, @FunctionalInterface, Stream, Optional 시작) TODO 정확하게 찾아볼 것
    * ㅁ 왜 사용하는가?
    *  ㅇ (가독성, 재사용성) 많은 데이터를 다룰 때, collection이나 array에 데이터를 담고, for 혹은 Iterator를 이용하여 제어
    *   -> 길고 가독성 떨어짐, 재사용성도 떨어진다.
    *  ㅇ (표준화 문제) Collection이나 Iterator 같은 인터페이스로 컬렉션을 다루는 방식을 표준화했지만,
    *   -> 각 컬렉션 클래스에 같은 기능을 가진 메서드들이 중복해서 정의되어 있음
    *   -> 데이터 소스마다 다른 방식으로 다뤄야함
    *   -> ex. List<String>, String[] - List를 정렬할 때는 Collections.sort()를 사용, 배열을 정렬할 때는 Arrays.sort()를 사용
    *  ===> Stream을 사용하면 이런 문제를 해결
    *  ㅇ 데이터 소스를 추상화, 데이터를 다루는 데에 자주 사용되는 메서드들을 정의
    *   -> 데이터 소스를 추상화 -> 어떤 데이터 소스든 같은 방식으로 다룰 수 있음 - 배열, 컬렉션, 파일에 저장된 데이터도 모두 같은 방식으로 다룰 수 있음
    *   -> 재사용성 증가
    *   -> ex. strList.stream(), Arrays.stream(strArr) -> Stream<String> 데이터 소스는 다르지만, stream으로 바꾼 뒤에는 동일한 방식으로 제어할 수 있다
    * 
    * ㅁ 특징
    *  ㅇ 데이터 소스를 변경하지 않음: 읽기 전용, 필요 시 새로운 메모리를 할당받는 데이터를 생성해서 반환
    *  ㅇ 일회용(Iterator처럼): 최종 연산 이후로는 닫혀서 다시 사용할 수 없음
    *  ㅇ 작업을 내부 반복으로 처리: 반복문을 메서드의 내부에 숨길 수 있다. - 수행할 작업은 functional interface를 인자로 받는다. ex. stream.forEach(~) 메서드 구현 확인
    *  ㅇ 스트림의 연산: 중간 연산 / 최종 연산 - 뒤에서 하나씩 사용할 것
    *  ㅇ 지연된 연산: 중간 연산 호출은 단지 어떤 작업이 수행되어야 하는지를 지정해주는 것 - 최종 연산이 호출되고 나서야 스트림의 요소들이 중간 연산을 거쳐 최종 연산에서 소모됨
    *  ㅇ 기본 타입 스트림: data source의 요소를 기본형으로 다루는 스트림 - 오토박싱 & 언박싱으로 인한 비효율 감소
    *   - ex. IntStream, LongStream, DoubleStream // ByteStream, ShortStream, CharStream, BooleanStream은 없음
    *   - cf. strings.chars()는 반환 타입이 IntStream // ByteArrayInputStream, ByteArrayOutputStream이 있으나 이건 그 stream이 아니라 io 패키지
    *   - *** 그 외의 Stream<T>의 요소들은 모두 기본 타입일 수 없음
    *  ㅇ 병렬 스트림: parallel()로 쉬운 병렬 처리 제공 cf.모든 스트림은 기본적으로 parallel이 아니라 sequential, parallel() sequential()은 새 스트림을 생성하는 것이 아니라, 스트림의 속성만 변경함
    *
    * ㅁ 스트림 만들기
    *  1. 컬렉션
    *  2. 배열
    *  3. 특정 범위의 정수
    *  4. 임의의 수
    *  5. 람다식 - iterate(), generate()
    *  6. 파일
    *  7. 빈 스트림
    *  8. 두 스트림 연결
    * */

    // p.818 컬렉션
    public void createStreamFromCollection() {
        System.out.println("collectionsAndFunctionalInterface()");
        // JCF의 최고 조상인 Collection 인터페이스의 stream() 메서드 - 해당 컬렉션을 source로 하는 스트림을 반환
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5); // cf. Arrays.a
        Stream<Integer> integerStream = list.stream(); // List를 source로 하는 컬렉션 생성
        integerStream.forEach(System.out::println);
        System.out.println("============================================================");
    }

    // p.819 배열
    public void createStreamOrIntStreamFromArray() {
        System.out.println("createStreamOrIntStreamFromArray()");

        String[] stringArray = {"a", "b", "c"};
        int[] intArray = {1, 2, 3, 4, 5};

        // 배열을 소스로 하는 스트림을 생성하는 메서드
        // (1) Stream.of(T... values) // 정확하게는 배열은 아니고 가변 인자(varargs)들
        Stream<String> strStream1 = Stream.of("a", "b", "c");
        Stream<Integer> integerStream1 = Stream.of(1, 2, 3, 4, 5);
        Stream<int[]> notIntegerStream = Stream.of(intArray); // Stream<Integer>가 아니라 Stream<int[]>가 됨 - Stream.of(T t) single element stream
        // cf. (IntStream 반환) IntStream.of(T... values)
        IntStream intStream1 = IntStream.of(1, 2, 3, 4, 5);

        // (2) (x) Stream.of(T[] array) (o) Stream.of(T... values) // 사실은 애초에 Stream.of(T[] array)라는 메서드가 없음 => 아래 이 메서드 아래 쪽 설명 참고
        // !주의: 매개변수 타입이 T[]이므로, int[] 같은 기본 타입 배열을 넣으면 Stream<Integer>를 만들지 못하고 Stream<int[]>가 된다.
        Stream<String> strStream2 = Stream.of(stringArray);
        // Stream<int[]> notIntegerStream = Stream.of(intArray); // -> 위의 Stream.of(T t) single element를 받는 메서드가 됨
        // cf. (IntStream 반환) (x) IntStream.of(T[] array) (o) IntStream.of(T... values)
        IntStream intStream2 = IntStream.of(intArray);

        // --- 위 Stream.of(T... values)는 단순히 return Arrays.stream(values);로 Array의 static method를 호출함

        // (3) Arrays.stream(T[] array)
        Stream<String> strStream3 = Arrays.stream(stringArray);
        // cf. (IntStream 반환) Arrays.stream(int[] array) 아예 기본 타입 배열을 파라미터로 받는 메서드가 따로 있기 때문에 (2)와는 다르게 기본 타입 배열 문제가 없음
        IntStream intStream3 = Arrays.stream(intArray);

        // (4) Arrays.stream(T[] array, int startInclusive, int endExclusive)
        Stream<String> strStream4 = Arrays.stream(stringArray, 0, 2);
        // cf. (IntStream 반환) Arrays.stream(int[] array, int startInclusive, int endExclusive)
        IntStream intStream4 = Arrays.stream(intArray, 0, 2);

        // ------------------------------------------------------------------------
        // cf. int[]를 Stream<Integer>로 바꾸기 - IntStream 생성 후 boxed()
        int[] ints = {1, 2, 3, 4, 5};
        Arrays.stream(intArray).boxed().forEach(System.out::println);

        // cf. Stream.of(T... t) 관련 // 참고 https://www.baeldung.com/varargs-vs-array // @SafeVarargs 관련 참고 https://www.baeldung.com/java-safevarargs
        // - 파라미터의 type이 가변 인자인 곳에는 배열을 넣을 수 있다.
        String[] exStrings = {"exEl1", "exEl2", "exEl3"};
        Stream<String> varargsEx1 = createStream("a", "b", "c"); // 파라미터의 type이 varargs인 곳에 여러 인자를 넣음
        Stream<String> varargsEx2 = createStream(exStrings); // 파라미터의 type이 varargs인 곳애 배열을 넘김
        Stream<String> varargsEx3 = Arrays.stream(exStrings); // 파라미터의 type이 배열인 곳에 배열을 넘김
        // - 반대로 파라미터의 type이 배열인 곳에 여러 인자를 넣을 수는 없다.
        // Arrays.stream("a", "b", "c"); // 컴파일 에러: 메서드 'stream(String, String, String)'를 해결할 수 없습니다
        
        // 위에서 보듯 파라미터의 type이 가변 인자인 곳에는 배열을 넣을 수 있지만, 가변 인자를 파라미터로 받는 메서드는 IDE에서 경고를 띄운다.
        // "매개변수화된 vararg 타입의 잠재적 힙 오염"
        // - 검사 정보: 가변 인자가 있는 메서드 중 @SafeVarargs 어노테이션을 추가할 수 있는 메서드를 모두 보고합니다.
        //            @SafeVarargs 어노테이션은 호출 사이트에서 매개변수화된 배열 생성에 관한 확인되지 않은 경고를 억제합니다.
        // 그래서 보통 varargs를 받는 메서드에는 @SafeVarargs 어노테이션이 따라다닌다. - Stream.of(T... t) 메서드도 마찬가지
        
        // 아무튼 Stream.of(T... t)인 곳에 String[]은 넘길 수 있다는 말, 그런데 왜 int[]는 넘길 수 없는가?
        // -> int는 generic type에 들어갈 수 없기 때문

        System.out.println("============================================================");
    }

    // 위 배열 스트림 관련 추가 메서드
    // @SafeVarargs
    private <T> Stream<T> createStream(T... values) { // Stream.of(T... values)와 똑같이 만든 메서드 - @SafeVarargs를 붙이지 않으면 검사 경고가 발생한다.
        return Arrays.stream(values);
    }

    // p.819 특정 범위의 정수
    public void createIntOrLongStreamByRange() {
        System.out.println("createIntOrLongStreamByRange()");

        IntStream range1 = IntStream.range(1, 5);
        range1.forEach(System.out::println);

        LongStream range2 = LongStream.rangeClosed(1, 4);
        range2.forEach(System.out::println);


        System.out.println("============================================================");
    }

    // p.820 임의의 수
    public void createRandomNumberString() {
        System.out.println("createRandomNumberString()");

        // Random 클래스의 instance method들로 임의의 기본 타입 숫자 스트림을 생성할 수 있다.
        // IntStream, LongStream, DoubleStream 타입들
        Random random = new Random();

        // infinite stream(무한 스트림)
        IntStream ints = random.ints();

        // 오버로딩된 다른 메서드를 이용해서 만들 때부터 유한 스트림으로 만들 수도 있다. - 인자는 개수를 의미
        LongStream longs = random.longs(Integer.MAX_VALUE + (long) 1);

        // begin, end로 지정된 범위의 난수를 생성할 수 있다. - origin은 inclusive, bound는 exclusive
        DoubleStream doubles = random.doubles(3.0, 11.0); // 무한 스트림
        IntStream intStream = random.ints(5, 1, 100); // 유한 스트림

        // ints.forEach(System.out::println); // 무한 스트림을 출력하면 콘솔에 계속 출력되고 안 멈춤
        intStream.forEach(System.out::println);

        System.out.println("============================================================");
    }

    public void createStreamByLambda() {
        System.out.println("createStreamByLambda()");
        // Stream의 두 static method
        // - public static <T> Stream<T> iterate(final T seed, final UnaryOperator<T> f)
        // - public static <T> Stream<T> generate(Supplier<? extends T> s)
        // cf. 위 메서드 반환타입 앞에 왜 <T>가 또 붙었는지는 아래 링크 중 "제네릭 메서드" 부분 참고
        // https://inpa.tistory.com/entry/JAVA-%E2%98%95-%EC%A0%9C%EB%84%A4%EB%A6%ADGenerics-%EA%B0%9C%EB%85%90-%EB%AC%B8%EB%B2%95-%EC%A0%95%EB%B3%B5%ED%95%98%EA%B8%B0
        Stream.iterate(0, n -> n + 2).limit(6).forEach(System.out::println);

        System.out.println("------------------------------------------------------------");
        // Stream.generate(() -> new Random().ints(10, 0, 101)).forEach(System.out::println); // (IDE 경고) 쇼트 서킷이 아닌 연산이 스트림을 무한으로 소비합니다
        // 위 경우, new Random().ints(~)는 int를 생성하는 것이 아니라 intStream을 생성하므로 의도와 맞지 않음...
        Stream.generate(() -> new Random().nextInt()).limit(5).forEach(System.out::println);

        System.out.println("------------------------------------------------------------");
        Stream.generate(Math::random).limit(5).forEach(System.out::println);

        System.out.println("------------------------------------------------------------");
        Stream.generate(UUID::randomUUID).limit(5).forEach(System.out::println);

        System.out.println("============================================================");
    }



    public void createStreamFromFilesAndPath() {
        System.out.println("createStreamFromFilesAndPath()");

        try (Stream<Path> pathStream = Files.list(Path.of("C:\\fastcampusSpringProject"))) {
            // cf. Path.of(~)에 들어갈 String은 "C:\\~" 형식이어도 되고 "C:/~" 형식이어도 됨 - "C:\" 형식은 안 됨 -> \ 자체가 "" 안에서 escape sequence를 만드는 표시이기 때문...
            pathStream.forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException("Files를 다루는 과정에서의 IOException", e);
        }

        System.out.println("============================================================");
    }

    // p.822 빈 스트림
    public void emptyStream() {
        System.out.println("emptyStream()");

        Stream<CustomClass> emptyStream1 = Stream.empty(); // 빈 스트림, generic type은 임의로 줘도 상관이 없다.
        emptyStream1.forEach(System.out::println);

        Stream<Void> emptyStream2 = Stream.empty();
        emptyStream2.forEach(System.out::println);

        System.out.println("============================================================");
    }

    // 위 빈 스트림 참고용 보조 클래스
    private static class CustomClass {
        Void noField;
    }

    // p.822 두 스트림의 연결
    public void createStreamByConcatenation() {
        System.out.println("createStreamByConcatenation()");

        int[] intArray = {1, 2, 3};
        IntStream intStream1 = IntStream.of(intArray);
        IntStream intStream2 = IntStream.range(90, 95);

        IntStream concat = IntStream.concat(intStream1, intStream2);
        // 물론 IntStream 뿐만 아니라 Stream<T>에도 concat(~) static method가 있음
        concat.forEach(System.out::println);

        System.out.println("============================================================");
    }
}
