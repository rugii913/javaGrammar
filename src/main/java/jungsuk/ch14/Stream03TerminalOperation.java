package jungsuk.ch14;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// ch.14 p.840 최종 연산 + p.828 기본 타입 스트림의 summaryStatistics()
public class Stream03TerminalOperation {

    // p.828 기본 타입 스트림만 갖고 있는 summaryStatistics() 메서드 - 여러 정보들을 한 번에 가져올 수 있다.
    // Stream02IntermediateOperation_Exs.class summaryStatisticsEx() 예제 14-10와 같은 내용
    // cf. 최종연산이다.
    public void mapToIntLongDouble_summaryStatistics() {
        System.out.println("mapToIntLongDouble_summaryStatistics() - Stream02IntermediateOperation.class mapToIntLongDouble() 관련");
        // 반환 타입으로는 IntSummaryStatistics, LongSummaryStatistics, DoubleSummaryStatistics 각각 존재함
        IntStream scoreStream = Arrays.stream(Stream02IntermediateOperation_Exs.students).mapToInt(Stream02IntermediateOperation_Exs.Student::getTotalScore);
        IntSummaryStatistics stat = scoreStream.summaryStatistics();

        long totalCount = stat.getCount();
        long totalScore = stat.getSum();
        double avgScore = stat.getAverage();
        int minScore = stat.getMin();
        int maxScore = stat.getMax();

        System.out.printf("""
                totalCount = %d
                totalScore = %d
                avgScore = %f
                minScore = %d
                maxScore = %d
                """, totalCount, totalScore, avgScore, minScore, maxScore);

        // System.out.println(scoreStream.sum()); // 이미 위에서 scoreStream.summaryStatistics(); 호출하며 최종 연산으로 scoreStream을 소모했으므로 에러

        System.out.println("============================================================");
    }

    // p.840 forEach - peek과 달리 스트림의 요소를 소모함
    public void forEach() {
        System.out.println("forEach()");
        
        String[] strArr = {
                "Inheritance", "Java", "Lambda", "stream", "OptionalDouble", "IntStream", "count", "sum"
        };

        Stream.of(strArr).forEach(System.out::println);

        System.out.println("============================================================");
    }

    // p.841 조건 검사
    // - allMatch(predicate), anyMatch(predicate), noneMatch(predicate): Predicate로 요소를 확인
    // - findFirst(), findAny(): (Optional<T>를 반환함에 유의) 주로 filter()와 함께 사용, 병렬 스트림이라면 findAny()를 사용해야함
    public void match_find() {
        System.out.println("match_find()");

        String[] strArr = {
                "Inheritance", "Java", "Lambda", "stream", "OptionalDouble", "IntStream", "count", "sum"
        };

        boolean noEmptyStr = Stream.of(strArr).noneMatch(s -> s.length() == 0);
        // boolean noEmptyStr = Stream.of(strArr).noneMatch(String::isEmpty);
        Optional<String> wordWhichStartsWithS = Stream.of(strArr).filter(s -> s.charAt(0) == 's').findFirst();

        System.out.println("noEmptyStr = " + noEmptyStr);
        System.out.println("wordWhichStartsWithS value= " + wordWhichStartsWithS.orElseThrow());

        System.out.println("============================================================");
    }

    // p.841 통계 - count(), sum(), average(), max(), min()
    public void count_sum_average_max_min() {
        System.out.println("count_sum_average_max_min()");

        // 기본 타입 스트림은 이 클래스 가장 위 메서드에서 볼 수 있는 것처럼 summaryStatistics()를 이용할 수 있다. // 혹은 Stream02IntermediateOperation_Exs.class의 summaryStatisticsEx()
        // summaryStatistics()를 이용하지 않더라도, count(), sum(), average(), max(), min()을 이용할 수 있다.

        // 반면 기본 타입 스트림이 아닌 경우, 통계와 관련된 메서드들이 아래 3개 뿐이다. (cf. max(~), min(~) 메서드도 Comparator 타입을 인자로 넘겨줘야 한다.)
        // count(), max(Comparator comparator), min(Comparator comparator)
        // ==> 대부분의 경우, 위 메서드를 사용하기 보다는
        // ====> 기본 타입 스트림으로 변환해서 통계 정보를 얻거나,
        // ====> 아래에서 볼 reduce(~) 혹은 다음에 볼 collect(~)를 사용해서 통계 정보를 얻는다.
        String[] strArr = {
                "Inheritance", "Java", "Lambda", "stream", "OptionalDouble", "IntStream", "count", "sum"
        };
        System.out.println("Arrays.toString(strArr) = " + Arrays.toString(strArr));
        System.out.println("Stream.of(strArr).count() = " + Stream.of(strArr).count());
        System.out.println("Stream.of(strArr).max(Comparator.naturalOrder()) = " + Stream.of(strArr).max(Comparator.naturalOrder()));
        System.out.println("Stream.of(strArr).min(Comparator.naturalOrder()) = " + Stream.of(strArr).min(Comparator.naturalOrder()));

        System.out.println("============================================================");
    }

    // p.841 리듀싱 - reduce(~)
    // 두 메서드가 있는데, 모두 BinaryOperator<T> accumulator는 필요함 - 직전 요소까지 연산한 결과에 이번 요소를 연산해서 쌓아간다는 느낌 - 스트림의 요소를 하나씩 소모하게 되며, 모든 요소를 소모하면 결과를 반환한다.
    // - Optional<T> reduce(BinaryOperator<T> accumulator): 빈 스트림이라든지, 요소가 하나 밖에 없다면, null Optional을 반환할 것
    // - T reduce(T identity, BinaryOperator<T> accumulator): identity가 있으므로 null safe, T 바로 반환
    // - <U> U reduce(U identity,
    //                 BiFunction<U, ? super T, U> accumulator,
    //                 BinaryOperator<U> combiner):
    //   cf. BinaryOperator extends BiFunction: 간략하게 보자면 BiFunction<T, T, T>인 경우가 BinaryOperator<T>라고 볼 수 있음
    //   cf. BinaryOperator<U> combiner는 병렬 스트림에 의해 처리된 결과를 합칠 때에 사용
    public void reduce() {
        System.out.println("reduce()");

        String[] strArr = {
                "Inheritance", "Java", "Lambda", "stream", "OptionalDouble", "IntStream", "count", "sum"
        };

        // count()와 같음: reduce(~)를 사용해서 intStream 요소의 개수를 세기
        IntStream intStream1 = Stream.of(strArr).mapToInt(String::length);
        int count = intStream1.reduce(0, (a, b) -> a + 1);

        // sum()과 같음: reduce(~)를 사용해서 intStream 요소의 합을 구하기
        IntStream intStream2 = Stream.of(strArr).mapToInt(String::length);
        int sum = intStream2.reduce(0, Integer::sum);
        // int sum = intStream2.reduce(0, (a, b) -> a + b);

        // max()와 같음: reduce(~)를 사용해서 intStream 요소 중 최댓값을 구하기
        IntStream intStream3 = Stream.of(strArr).mapToInt(String::length);
        OptionalInt max = intStream3.reduce(Integer::max); // 파라미터 하나짜리 reduce(~)는 Optional을 반환함
        // int max = intStream3.reduce(0, Math::max);

        // min()과 같음: reduce(~)를 사용해서 intStream 요소 중 최솟값을 구하기
        IntStream intStream4 = Stream.of(strArr).mapToInt(String::length);
        int min = intStream4.reduce(0, Integer::min); // 파라미터 두 개짜리 reduce(~)는 Optional을 반환하지 않음

        System.out.println("count = " + count);
        System.out.println("sum = " + sum);
        System.out.println("max.getAsInt() = " + max.getAsInt()); // OptionalInt로 받아오므로, 꺼내오는 메서드 필요
        System.out.println("min = " + min);

        System.out.println("============================================================");
    }
}
