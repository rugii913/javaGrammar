package jungsuk.ch14;

import java.io.File;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// ch.14 p.823 중간 연산
public class Stream02IntermediateOperation {

    // p.823 자르기
    public void skip_limit() {
        System.out.println("skip_limit()");

        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        list.stream().skip(5).forEach(System.out::print);
        System.out.println();
        list.stream().limit(5).forEach(System.out::print);
        System.out.println();

        // Stream<T> 말고 기본 타입 스트림에도 정의되어 있음
        IntStream.rangeClosed(1, 10).skip(5).forEach(System.out::print);
        System.out.println();
        IntStream.rangeClosed(1, 10).limit(5).forEach(System.out::print);
        System.out.println();

        System.out.println("============================================================");
    }

    // p.823 걸러내기
    public void filter_distinct() {
        System.out.println("filter_distinct()");

        // distinct() - 스트림에서 중복된 요소들을 제거
        Stream.of("a", "b", "c", "d", "a").distinct().forEach(System.out::print); // 출력 결과: abcd
        System.out.println();
        // cf. 직관적으로 당연히 그럴 것 같긴 하지만... 중복된 요소 판별 기준은 equals(~) 메서드이다.
        // distinct() 메서드 상세 설명 - Returns a stream consisting of the distinct elements (according to Object.equals(Object)) of this stream.
        Stream.of(ClassOverridingEqualsAndHashCode.of(1), ClassOverridingEqualsAndHashCode.of(1)).distinct().forEach(System.out::print); // 출력 결과: 1
        System.out.println();
        Stream.of(ClassNotOverridingEqualsAndHashCode.of(1), ClassNotOverridingEqualsAndHashCode.of(1)).distinct().forEach(System.out::print); // 출력 결과: 11
        System.out.println();

        System.out.println("------------------------------------------------------------");
        // filter()는 Predicate 타입 인자 필요
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        list.stream().filter(i -> i % 2 == 0).forEach(System.out::print);
        System.out.println();
        IntStream.rangeClosed(1, 10).filter(i -> i % 2 != 0).filter(i -> i % 3 == 0).forEach(System.out::print);
        System.out.println();

        System.out.println("============================================================");
    }

    // 위 filter_distinct()를 위한 보조 클래스 - equals(~)를 overriding 한 경우
    static class ClassOverridingEqualsAndHashCode {
        int field;

        private ClassOverridingEqualsAndHashCode(int field) {
            this.field = field;
        }

        public static ClassOverridingEqualsAndHashCode of(int field) {
            return new ClassOverridingEqualsAndHashCode(field);
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof ClassOverridingEqualsAndHashCode that)) return false;
            return field == that.field;
        }

        @Override
        public int hashCode() {
            return Objects.hash(field);
        }

        @Override
        public String toString() {
            return String.valueOf(field);
        }
    }

    // 위 filter_distinct()를 위한 보조 클래스 - equals(~)를 overriding 하지 않은 경우
    static class ClassNotOverridingEqualsAndHashCode {
        int field;

        private ClassNotOverridingEqualsAndHashCode(int field) {
            this.field = field;
        }

        public static ClassNotOverridingEqualsAndHashCode of(int field) {
            return new ClassNotOverridingEqualsAndHashCode(field);
        }

        @Override
        public String toString() {
            return String.valueOf(field);
        }
    }

    // p.824 정렬
    // 관련 예제 14-8 -> Stream02IntermediateOperation_Exs의 sortedEx()
    public void sorted() {
        System.out.println("sorted()");
        System.out.println("""
                복잡해 보이지만 여기 sorted(~)에 인자로 넘긴 것들은 모두 일정한
                Comparator 타입 미리 정의된 객체들이거나,
                Comparator 타입 객체를 반환하는 메서드들이다.
                => Stream02_Aux_ComparatorInterface.class에 적어놓은 것 참고
                """);
        // - 이름이 같은 두 메서드 sorted(Comparator 인스턴스를 받지 않냐, 받냐 차이)
        // Stream<T> sorted() - Stream의 요소(generic type T)이 compareTo를 구현한 Comparable인 경우 사용 가능 ex.String, File, LocalDateTime, ...
        //                    - 요소가 Comparable이 아닌 경우 에러 ex.LinkedList, ...
        // Stream<T> sorted(Comparator<? super T> comparator) - T에 적용할 수 있는 Comparator를 인자로 넘김 - ? super T이므로 T의 조상 클래스의 comparator를 넘길 수도 있음

        // cf. Comparator에 대한 분석은 Stream02_Aux_ComparatorAndComparatorInCollections에 적어두었다.

        String[] strings = {"ba-", "aaa-", "CC-", "Cc-", "cc-", "b-", "ZabC-"};
        StringBuilder sb = new StringBuilder();

        // (1) sorted(): 기본 정렬(string.compareTo()가 사용된 것 - 내부 구현에서는 byte[] value 비교, length 비교 등으로 판단)
        Stream.of(strings).sorted().forEach(sb::append);
        sb.deleteCharAt(sb.length() - 1);
        sb.append("\n");

        // (2) sorted(Comparator.naturalOrder()): 기본 정렬
        // return (Comparator<T>) Comparators.NaturalOrderComparator.INSTANCE;
        // (=> NaturalOrderComparator는 Comparator 인터페이스를 지원하는 클래스 Comparators의 내부 enum - INSTANCE라는 인스턴스를 하나 갖고 있음)
        // -> public int compare(Comparable<Object> c1, Comparable<Object> c2) {return c1.compareTo(c2);} // 까보면 결국 Comparable의 compareTo를 호출함
        Stream.of(strings).sorted(Comparator.naturalOrder()).forEach(sb::append);
        sb.deleteCharAt(sb.length() - 1);
        sb.append("\n");

        // (3) sorted((s1, s2) -> s1.compareTo(s2)): 기본 정렬, 람다식을 통해 Comparator를 Comparable인 String이 이미 갖고 있는 compareTo(T t)로 구현한 형태
        Stream.of(strings).sorted((s1, s2) -> s1.compareTo(s2)).forEach(sb::append);
                // cf. (IDE 경고) 검사 정보: Comparator.comparing() 호출을 사용하여 표현할 수 있는 람다 식으로 정의된 Comparator 인스턴스를 보고합니다. Comparator.thenComparing() 식으로 바꿀 수 있는 체인 비교 또한 보고됩니다.
        sb.deleteCharAt(sb.length() - 1);
        sb.append("\n");

        // (4) sorted(String::compareTo): 기본 정렬 - Comparable인 String이 이미 갖고 있는 compareTo를 메서드 참조로 불러와서 Comparator로 사용
        Stream.of(strings).sorted(String::compareTo).forEach(sb::append); // 메서드 참조로 바꾸었을 뿐 위와 동일함
        sb.deleteCharAt(sb.length() - 1);
        sb.append("\n");

        sb.append("------------------------------------------------------------\n");
        // (5) sorted(Comparator.reverseOrder()): 기본 정렬의 역순 Comparator.reverseOrder() returns Collections.reverseOrder() => (5-1)
        Stream.of(strings).sorted(Comparator.reverseOrder()).forEach(sb::append);
        sb.deleteCharAt(sb.length() - 1);
        sb.append("\n");

        // (5-1) 책에는 안 써두었지만, 위와 같다.
        // Collections.reverseOrder() => public static <T> Comparator<T> reverseOrder() return (Comparator<T>) ReverseComparator.REVERSE_ORDER;
        // Collections의 내부 클래스 ReverseComparator 중 (cf. ReverseComparator는 Comparable이어야만 가능함)
        // static final ReverseComparator REVERSE_ORDER = new ReverseComparator();
        // public int compare(Comparable<Object> c1, Comparable<Object> c2) return c2.compareTo(c1);
        Stream.of(strings).sorted(Collections.reverseOrder()).forEach(sb::append);
        sb.deleteCharAt(sb.length() - 1);
        sb.append("\n");

        // (6) sorted(Comparator.<String>naturalOrder().reversed()): 기본 정렬의 역순
        // 위 (2)에서 언급한 NaturalOrderComparator enum 인스턴스가 갖고 있는 reversed() 메서드를 이용함
        Stream.of(strings)
                // .sorted(Comparator.naturalOrder().reversed()).forEach(System.out::print); // (컴파일 에러) 필요 타입: Comparator <? super String> 제공된 타입: Comparator <T>
                // -> naturalOrder() 자체가 generic method인데, 여기서 간단하게 타입 추론이 안 되므로, 생성할 때 타입을 지정해줘야 한다.
                .sorted(Comparator.<String>naturalOrder().reversed()).forEach(sb::append);
        sb.deleteCharAt(sb.length() - 1);
        sb.append("\n");

        sb.append("------------------------------------------------------------\n");
        // (7) sorted(String.CASE_INSENSITIVE_ORDER): 대소문자 구분 없는 기본 정렬
        Stream.of(strings).sorted(String.CASE_INSENSITIVE_ORDER).forEach(sb::append);
        sb.deleteCharAt(sb.length() - 1);
        sb.append("\n");

        sb.append("------------------------------------------------------------\n");
        // (8) sorted(String.CASE_INSENSITIVE_ORDER.reversed()): 대소문자 구분 없는 역순 정렬
        Stream.of(strings).sorted(String.CASE_INSENSITIVE_ORDER.reversed()).forEach(sb::append);
        sb.deleteCharAt(sb.length() - 1);
        sb.append("\n");

        sb.append("------------------------------------------------------------\n");
        // (9) sorted(Comparator.comparing(String::length)): 각 String의 길이 순 정렬
        Stream.of(strings).sorted(Comparator.comparing(String::length)).forEach(sb::append);
        sb.deleteCharAt(sb.length() - 1);
        sb.append("\n");

        // (10) sorted(Comparator.comparingInt(String::length)): 각 String의 길이 순 정렬 - wrapper class Integer 없이 바로 int로 비교
        Stream.of(strings).sorted(Comparator.comparingInt(String::length)).forEach(sb::append);
        sb.deleteCharAt(sb.length() - 1);
        sb.append("\n");

        sb.append("------------------------------------------------------------\n");
        // (11) sorted(Comparator.comparingInt(String::length).reversed()): 각 String의 길이 역순 정렬 - wrapper class Integer 없이 바로 int로 비교
        // sorted(Comparator.comparing(String::length).reversed()): Integer로 비교해도 같다.
        Stream.of(strings).sorted(Comparator.comparingInt(String::length).reversed()).forEach(sb::append);
        sb.deleteCharAt(sb.length() - 1);
        sb.append("\n");

        System.out.println(sb);
        System.out.println("============================================================");
    }

    // p.827 변환
    // - 스트림의 요소에 저장된 값 중에서 원하는 필드만 뽑아내거나
    // - 스트림의 요소를 특정 형태로 변환 - ex. IntStream을 index처럼 사용한 뒤 mapping할 수 있음
    // 관련 예제 14-9 -> Stream02IntermediateOperation_Exs의 mapEx()
    public void map() {
        System.out.println("map()");

        Stream<File> fileStream = Stream.of(new File("Ex1.java"), new File("Ex1"),
                new File("Ex1.bak"), new File("Ex2.java"), new File("Ex1.txt"));

        // map()으로 각 스트림 요소 File의 pathName만 뽑아내서 Stream<File>을 Stream<String>으로 변환
        Stream<String> fileNameStream = fileStream.map(File::getName);
        fileNameStream.forEach(System.out::println);
        // 더 나아간 예제는 Stream02IntermediateOperation_Exs의 mapEx() 확인

        // public abstract <R> Stream<R> map(java.util.function.Function<? super T, ? extends R> mapper)
        // => 여기의 mapper는 T의 조상 타입을 매개변수로 받아서 R을 상속하는 타입 객체를 돌려주는 Function이다(함수형 인터페이스, 주로 람다식 혹은 메서드 참조로 정의)
        // ==> map(~)은 Stream<T> 타입 stream이 호출해서 그 결과 Stream<R> 타입 stream이 된다.
        System.out.println("============================================================");
    }

    // p.827 조회
    // - forEach()와 달리 스트림의 요소를 소모하지 않음
    // - 어떤 식으로 출력되는지 확인해서 stream의 요소가 어떤 순서로 다뤄지는지 추측해보자.
    public void peek() {
        System.out.println("peek()");

        Stream<File> fileStream = Stream.of(new File("Ex1.java"), new File("Ex1"),
                new File("Ex1.bak"), new File("Ex2.java"), new File("Ex1.txt"));

        fileStream.map(File::getName) // Stream<File> -> Stream<String>
                // cf. 책에서는 filter(~) 후 peek(~) 하는데, 나는 의심되는 부분이 있어서 peek(~) 후에 filter(~)를 하도록 조정했다.
                .peek(s -> System.out.printf("filename = %s%n", s)) // 파일명을 출력한다.
                .filter(s -> s.indexOf('.') != -1) // 확장자가 없는 것은 제외
                .map(s -> s.substring(s.indexOf('.') + 1)) // 확장자만 추출
                .peek(s -> System.out.printf("extension = %s%n", s)) // 확장자를 출력한다.
                .forEach(System.out::println);

        System.out.println("============================================================");
    }

    // p.828 기본 타입 스트림으로 변환
    public void mapToIntLongDouble() {
        System.out.println("mapToIntLongDouble()");

        // Stream<Stream02IntermediateOperation_Exs.Student> studentStream = Stream.of(Stream02IntermediateOperation_Exs.students);
        // cf. 참조형이므로 Arrays.stream(~) 말고, Stream.of(~)도 가능
        Stream<Stream02IntermediateOperation_Exs.Student> studentStream = Arrays.stream(Stream02IntermediateOperation_Exs.students);

        // 애초에 Stream<Integer>가 아니라 IntStream으로 받는 게 유리한 경우라면 map(~)이 아니라 mapToInt(~)를 사용하는 편이 낫다.
        // ex. 단순히 요소들을 더하는 경우 - 기본 타입 스트림이 제공하는 sum() 메서드 사용
        // Stream<Integer> studentScoreStream = studentStream.map(Stream02IntermediateOperation_Exs.Student::getTotalScore);
        // -> Stream<Integer>에는 sum() 같은 편리한 메서드가 없다.
        IntStream studentScoreStream = studentStream.mapToInt(Stream02IntermediateOperation_Exs.Student::getTotalScore);
        System.out.println(studentScoreStream.sum());

        System.out.println("""
                기본 타입 스트림은 아래와 같은 편리한 메서드를 제공
                int sum()
                OptionalDouble average()
                OptionalInt max()
                OptionalInt min()
                (cf.) Stream에도 max와 min이 있긴 한데
                Optional<T> max(Comparator<? super T> comparator)
                Optional<T> min(Comparator<? super T> comparator)
                으로 comparator를 지정해줘야 한다.
                (cf.) 스트림의 요소가 없을 때 sum()은 0을 반환하고 끝, 나머지 메서드들은 null과 관련된 처리가 필요하므로 Optional 사용
                """);

        System.out.println();
        System.out.println("cf. 각 기본 타입 스트림은 summaryStatistics()라는 최종 연산으로 max, min, agv, sum 등의 값을 한 번에 구할 수 있음 " +
                "  - Stream02IntermediateOperation_Exs.class summaryStatisticsEx() 참고" +
                "  - 또는 Stream03TerminalOperation.class mapToIntLongDouble_summaryStatistics() 참고");
        System.out.println("기본 타입 스트림에서 wrapper 타입 요소 스트림으로 바꾸고 싶을 때는 중간 연산 boxed() 사용");
        System.out.println("기본 타입 스트림에서 다른 타입 요소 스트림으로 바꾸고 싶을 때는 중간 연산 mapToObj(~) 사용");
        System.out.println("스트림 변환 정리 표 -> p.864");

        // ex. 로또 번호 - int을 String으로 변환
        IntStream intStream = new Random().ints(1, 46);
        Stream<String> lottoStream = intStream.distinct().limit(6).sorted().mapToObj(i -> i + ",");
        lottoStream.forEach(System.out::println);

        // ex. CharSequence의 chars() 메서드는 IntStream을 반환
        IntStream charCodePointStream = "12345".chars();
        int charSum = charCodePointStream.map(ch -> ch - '0').sum();
        
        // cf. Stream<String> -> IntStream 변환할 때는 mapToInt(Integer::parseInt),
        //     Stream<Integer> -> IntStream 변환할 때는 mapToInt(Integer::intValue) // 혹은 mapToInt(i -> i) 이런 식으로 주는 것도 가능함 // 다만 mapToInt(Function.identity()) 이건 못 넘긴다.

        System.out.println("============================================================");
    }

    // p.831 평면화 - Stream<T[]>를 Stream<T>로 변환 (2차원을 1차원으로 만든다고 생각해볼 수 있을 것 같다.)
    public void flatMap() {
        System.out.println("flatMap()");

        Stream<String[]> strArrStream = Stream.of(new String[]{"abc", "def", "jkl"}, new String[]{"ABC", "GHI", "JKL"});

        // 아래처럼 하면 안 된다.
        // Stream<Stream<String>> streamStream = strArrStream.map(array -> Arrays.stream(array));
        // strArrStream의 요소는 String[]
        // => strArrStream.map(Arrays::stream)은 strArrStream의 각 요소인 String[] strArr1, String[] strArr2, ...에 대해서
        //    Arrays.stream(strArr1), Arrays.stream(strArr2), ... 을 요소로 하는 스트림을 만드는 것이 됨
        // => Arrays.stream(strArr1), ... 는 Stream<String> 타입이 되므로 결과적으로
        //    Stream<Stream<String>> 타입이 된다.
        Stream<String> strStream = strArrStream.flatMap(Arrays::stream);
        strStream.forEach(System.out::println);

        /*
         * 정리하자면
         * 처음 상태: Stream<String[]> - 요소가 String[]인 스트림
         * 바라는 것: Stream<String> - 요소가 String인 스트림
         *
         * map(~)의 메서드 시그니처: <R> Stream<R> map(Function<? super T, ? extends R> mapper);
         * flatMap(~)의 메서드 시그니처: <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);
         *
         * 정확한 구현은 찾아봐야하지만, Stream의 docs를 읽어보면
         * map(~)은 mapper에 의해 각 T 타입 요소가 각 R 타입 요소로 매핑된 결과를 요소로 하는 스트림을 반환하고
         * flatMap(~)은 mapper가 Stream<T>의 각 T 타입 요소들을 어떤 Stream<R>(R 타입 요소들을 가진 스트림)로 매핑할 것인데,
         *             그 R 타입 요소들을 요소로 갖는 스트림 Stream<R>을 반환한다.
         * ==> map(~) 하면 Stream<Stream<R>>가 나올 곳에, flatMap(~) 하면 Stream<R>이 나온다.
         * */
        System.out.println("많은 예제는 Stream02IntermediateOperation_Exs.class flatMap() 참고 - p.834 예제 14-11");

        System.out.println("============================================================");
    }
}
