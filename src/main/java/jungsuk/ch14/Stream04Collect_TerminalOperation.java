package jungsuk.ch14;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

// ch.14 p.844 collect()
public class Stream04Collect_TerminalOperation {

    // Collector 사용해보기
    public void collectEx() {
        System.out.println("collectEx() - Collector 사용해보기");
        // Stream.of(~).collect(~); stream의 collect(~) 메서드는 2개
        // (1) <R, A> R collect(Collector<? super T, A, R> collector) - Collector 자체를 넘기는 것 - Collectors 클래스의 static 메서드에서 미리 구현된 Collector를 얻어올 수 있다.
        // (2) <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner);
        //  - supplier, accumulator, combiner를 넘김(람다식으로 구현하면 편리) - Collector를 구현하는 것과 거의 유사
        // -> (1) Collector를 사용하거나, (2) Collector와 비슷한 일을 할 메서드 세 개 구현하거나 // sort()에 Comparator가 필요한 것처럼 collect(~)에는 Collector가 필요

        System.out.println("이미 있는 Collector를 사용한 경우");
        // String collect = Stream.of("a", "b", "c").collect(joining()); // (IDE 경고) 'String.join'(으)로 바꾸기 가능
        String collectedString1 = Stream.of("a", "b", "c").collect(joining(", ", "{", "}"));
        System.out.println("collectedString1 = " + collectedString1);

        System.out.println("이미 있는 Collector를 사용하지 않고, collect() 작업에 필요한 메서드들을 구현해서 넘겨준 경우");
        StringBuilder collectStringBuilder2 = Stream.of("a", "b", "c").collect(StringBuilder::new, (sb, s) -> sb.append(", ").append(s), StringBuilder::append);
        String collectedString2 = collectStringBuilder2.toString();
        System.out.println("collectedString2 = " + collectedString2);
        
        System.out.println("============================================================");
    }

    // 책과 순서 다르게 인터페이스 구현 먼저 해봄
    
    // p.861 Collector 인터페이스 구현하기 - 예제 14-17 관련
    // 필수로 구현해야하는 메서드는 다섯 개 - characteristics()를 제외하면 나머지 메서드는 모두 반환 타입이 functional interface
    //                                -> 람다식 네 개 작성하면 된다.
    // Collector<T, A, R>: T 타입 요소를 받아서, A 타입으로 변환 및 reduction 작업한 후, R 타입으로 내보냄
    // <T> – the type of input elements to the reduction operation: 스트림의 요소 타입
    // <A> – the mutable accumulation type of the reduction operation (often hidden as an implementation detail): T를 A로 변환해서 작업
    // <R> – the result type of the reduction operation: 최종 결과 타입
    // 1. Supplier<A> supplier() - 작업 결과를 저장할 공간을 제공(A: T를 A로 변환해서 작업)
    // 2. BiConsumer<A, T> accumulator() - 스트림의 요소를 수집(collect)할 방법을 제공(A: T를 A로 변환 후 reduction 작업, T: 스트림의 요소 타입)
    // 3. BinaryOperator<A> combiner() - 병렬 스트림에서 두 저장 공간을 병합할 방법을 제공(A: T를 A로 변환해서 작업)
    // 4. Function<A, R> finisher() - 결과를 최종적으로 변환할 방법을 제공(A: T를 A로 변환 후 reduction 작업한 것, R: 최종 결과 타입) // 변환이 필요 없다면, Function.identity()를 넘기면 된다.
    // 5. Set<Characteristics> characteristics() - 컬렉터가 수행하는 작업의 속성에 대한 정보를 제공, enum을 Set에 담아서 반환
    //    - Collector의 내부 enum Characteristics를 사용
    //    - (1)Characteristics.CONCURRENT: 병렬 작업 (2)Characteristics.UNORDERED: 스트림 요소 순서 유지 필요 없는 작업 (3)Characteristics.IDENTITY_FINISH: finisher()가 항등함수
    //    - 아무 속성도 지정하고 싶지 않다면 Collections.emptySet() 반환하면 됨
    // ==> finisher, characteristics를 제외하고는 p.841 reduce(~) 설명에서 본 것
    //     - reduce(~)와 collect(~)는 유사하다.
    //     - 위 T, A, R 타입 관련 docs 복붙 내용에서도 볼 수 있듯 reduction operation이라는 표현을 사용한다.
    //     - 다만 collect는 그룹화, 분할, 병렬 작업에 유용
    public void implementAConcatCollectorEx() {
        System.out.println("implementAConcatCollectorEx() - Collector 구현(Stream<String> 요소 이어붙이기)");

        Collector<String, StringBuilder, String> concatCollector = new Collector<>() {
            @Override
            // A function that creates and returns a new mutable result container. - reduction 작업물을 담을 저장소를 생성하고 반환 (원소 하나 씩 훑으면서 reduction 작업해야 하므로 mutable이어야 함)
            // 반환: a function which returns a new, mutable result container
            public Supplier<StringBuilder> supplier() {
                // return () -> new StringBuilder()
                return StringBuilder::new;
            }

            @Override
            // A function that folds a value into a mutable result container. - reduction 작업 - supplier()로 만들어진 container에 담는다.
            // 반환: a function which folds a value into a mutable result container
            public BiConsumer<StringBuilder, String> accumulator() {
                // return (sb, s) -> sb.append(s);
                return StringBuilder::append;
            }

            @Override
            // A function that accepts two partial results and merges them.
            // The combiner function may fold state from one argument into the other and return that, or may return a new result container. - 병렬 작업 시 합칠 때 사용
            // 반환: a function which combines two partial results into a combined result
            public BinaryOperator<StringBuilder> combiner() {
                // return (sb1, sb2) -> sb1.append(sb2);
                return StringBuilder::append;
            }

            @Override
            // Perform the final transformation from the intermediate accumulation type A to the final result type R.
            // If the characteristic IDENTITY_FINISH is set, this function may be presumed to be an identity transform with an unchecked cast from A to R. - 마지막 반환 시 A -> R로 타입을 바꿔주기
            // 반환: a function which transforms the intermediate result to the final result
            public Function<StringBuilder, String> finisher() {
                // return sb -> sb.toString();
                return StringBuilder::toString;
            }

            @Override
            // Returns a Set of Collector. Characteristics indicating the characteristics of this Collector. This set should be immutable.
            // 반환: an immutable set of collector characteristics
            public Set<Characteristics> characteristics() {
                return Collections.emptySet(); // 아무런 속성도 지정하지 않은 것 - 병렬 작업 x, 순서 유지 필요 없음 x, finisher()가 항등함수 x
                // 원래는 Collector의 특성이 담긴 Set을 반환하는 것으로 의도되어 있음
            }
        };

        String[] exStrings = {"aaa", "bbb", "ccc"};
        System.out.println("exStrings = " + Arrays.toString(exStrings));
        String result = Stream.of(exStrings).collect(concatCollector);
        System.out.println("result = " + result); // String[]의 요소들이 concat된 것을 확인

        System.out.println("============================================================");
    }

    // p.844 ~ 847 collect() / toList(), toSet(), toMap(), toCollection(), toArray() / counting(), summingInt(), averagingInt(), maxBy(), minBy() / reducing() / joining()
    // p.847 예제 14-14 관련
    public void collectBasics() {
        System.out.println("collectBasics() - Collectors에서 static methods의 반환으로 제공하는 이미 만들어진 간단한 Collector들을 사용해보기");
        System.out.println("""
        collect(), Collector, Collectors (p.844)
        toArray() / toList(), toSet(), toMap(), toCollection() - 스트림을 배열과 컬렉션으로 변환 (p.845)
        counting(), summingInt(), averagingInt(), maxBy(), minBy(), SummarizingInt() - 통계 (p.846)
        reducing() - 리듀싱 (p.846)
        joining() - 문자열 결합 (p.847)
        """);

        //

        System.out.println();
        System.out.println("toArray() / toList(), toSet(), toMap(), toCollection() - Collectors의 static 메서드를 사용하여 Collector를 얻어오기");
        System.out.println("toArray() - 스트림을 배열로 변환 후 enhanced for로 출력해봄");
        System.out.println("이 경우 Collector를 사용하는 게 아니라 Stream에 있는 toArray() 메서드를 사용한다.");
        Student[] newStudentArray = Stream.of(studentArray).toArray(Student[]::new);
        for (Student student : newStudentArray) {
            System.out.println(student);
        }

        System.out.println();
        System.out.println("collect(Collectors.toList()) - 학생 이름만 뽑아서 List<String>으로 저장 후 출력해봄");
        System.out.println("cf. Java 16 이후로 Stream에서 바로 toList() 사용 가능");
        List<String> names = Stream.of(studentArray).map(Student::name).collect(toList());
        // List<String> names = Stream.of(studentArray).map(Student::name).toList(); // cf. Java 16 이상부터는 Stream에서 바로 toList() 사용 가능
        System.out.println(names);
        // cf. -> 여기서 왜 List 안의 요소들이 출력되는지? *********************************************************
        //     -> List, Collection은 기본적으로 toString()을 갖고 있지 않음 ***interface이므로 Object와는 관련이 없다.
        //     --> 그런데 이런 식으로 만드는 List 객체를 getClass() 해보면
        //         java.util.ImmutableCollections$ListN라고 나옴
        //     ---> ListN -> ImmutableCollections -> AbstractCollection에서 toString()을 override해서 구현하고 있음

        System.out.println();
        System.out.println("collect(Collectors.toMap()) - 스트림을 Map<Integer, Student>로 변환");
        // cf. 샘플 데이터를 p.857 데이터를 사용하고 있어서 학생 이름 중복 있음 - 에러 발생
        //     - 학생 이름이 key인 구조에서 id값 추가했음(AtomicLong incrementAndGet() 사용
        // Map<Integer, Student> studentMap = Stream.of(studentArray).collect(toMap(s -> s.id, studentObj -> studentObj)); // studentObj -> studentObj 부분을 항등함수로 바꿀 수 있다.
        Map<Integer, Student> studentMap = Stream.of(studentArray).collect(toMap(s -> s.id, Function.identity()));
        for (int id : studentMap.keySet()) {
            System.out.println(studentMap.get(id).name + "-" + studentMap.get(id));
        }

        System.out.println();
        System.out.println("collect(Collectors.toCollection(Supplier collectionFactory)) - List, Set, Map 인터페이스가 아닌, 특정 자료구조로 변환할 때");
        // 해당하는 컬렉션의 생성자 참조를 매개변수로 넣어주면 된다.
        ArrayList<Student> studentArrayList = Stream.of(studentArray).collect(toCollection(ArrayList::new));
        for (Student student : studentArrayList) {
            System.out.println(student);
        }

        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("counting(), summingInt(), averagingInt(), maxBy(), minBy() - 통계"); // 이들 자체는 다른 연산으로 대체해도 상관 없는 것들 - groupingBy()와 함께 사용할 때 유용해짐
        Long count = Stream.of(studentArray).collect(counting()); // (IDE 경고) 'collect(counting())'을(를) 'count()'(으)로 바꿀수 있습니다
        System.out.println("count = " + count);

        Integer totalScore = Stream.of(studentArray).collect(summingInt(Student::score)); // (IDE 경고) 'collect(summingInt())'을(를) 'mapToInt().sum()'(으)로 바꿀수 있습니다
        System.out.println("totalScore = " + totalScore);

        Optional<Student> topStudent = Stream.of(studentArray).collect(maxBy(Comparator.comparingInt(Student::score))); // (IDE 경고) 'collect(maxBy())'을(를) 'max()'(으)로 바꿀수 있습니다(의미가 변경될 수 있음)
        System.out.println("topStudent = " + topStudent.get()); // (IDE 경고) 'isPresent()' 검사가 없는 'Optional.get()'

        IntSummaryStatistics statistics = Stream.of(studentArray).collect(summarizingInt(Student::score));
        System.out.println("statistics: " + statistics);

        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("reducing() - 리듀싱");
        // (1) <T> Collector<T, ?, Optional<T>> reducing(BinaryOperator<T> op)
        // (2) <T> Collector<T, ?, T> reducing(T identity, BinaryOperator<T> op)
        // (3) <T, U> Collector<T, ?, U> reducing(U identity, Function<? super T, ? extends U> mapper, BinaryOperator<U> op)
        // 예제에서는 (3)을 사용해봄 - map(~)과 reduce(~)를 하나로 합친 형태 -> stream.reduce(~)와 비교해보면, 스트림 요소에서 값을 뽑아내는 mapper 사용할 수 있는 메서드가 있음
        // cf. 이 경우 summingInt(~) 사용한 것과 결과 같음
        totalScore = Stream.of(studentArray).collect(reducing(0, Student::score, Integer::sum)); // (IDE 경고) 'collect(reducing())'을(를) 'map().reduce()'(으)로 바꿀수 있습니다
        System.out.println("totalScore = " + totalScore);

        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("joining() - 문자열 결합");
        // 문자열 스트림의 모든 요소를 하나의 문자열로 연결해서 반환 - 구분자(delimiter), 접두사(prefix), 접미사(suffix) 지정 가능
        // 스트림의 요소가 CharSequence의 자손인 경우 결합 가능
        String studentsNamesString = Stream.of(studentArray).map(Student::name).collect(joining(",", "{", "}"));
        System.out.println("studentsNamesString = " + studentsNamesString);

        System.out.println("============================================================");
    }

    /*
    * p.849 그룹화와 분할 - partitioningBy(), groupingBy() - collect()를 사용하는 이유
    * 그룹화와 분할은 분류를 Function으로 하느냐, Predicate으로 하느냐의 차이만 있을 뿐 동일함 - 결과도 둘 다 Map으로 담겨 반환된다.
    * */
    // p.852 예제 14-15 관련
    public void partitioningByEx() {
        System.out.println("partitioningByEx()");
        System.out.println("partitioningBy는 Predicate을 사용 - 스트림을 두 개의 그룹으로 나눠야 하는 경우");

        // (1) Collector<T, ?, Map<Boolean, List<T>>> partitioningBy(Predicate<? super T> predicate)
        //     -> 아래 (2) 메서드에 Collector<? super T, A, D> downstream을 Collectors.toList()를 넘긴 것
        //     -> 각 요소에 대한 Predicate 결과값 true/false로 partition하고 partition된 요소들을 List로 collect
        // (2) <T, D, A> Collector<T, ?, Map<Boolean, D>> partitioningBy(Predicate<? super T> predicate, Collector<? super T, A, D> downstream)
        //     -> 각 요소에 대한 Predicate 결과값 true/false로 partition하고 partition된 요소들을 Collector에 따라 reduction 작업
        // ===> Map의 value의 타입은 Collector downstream의 finisher의 반환 타입을 따라 간다. (아마도 predicate에 의한 partition 판단 후 후속 작업이라는 의미로 downstream이라는 이름이 붙은 듯하다.)
        // =====> cf. 3에서 사용한 collectingAndThen(~)은 collect 후의 또 다른 후처리 작업이라고 생각하면 될 듯하다.

        System.out.println("1. 단순분할(성별로 분할)");
        Map<Boolean, List<Student>> listMapPartitionedByGender =
                Stream.of(studentArray)
                        .collect(partitioningBy(student -> student.gender.equals(Gender.MALE)));

        List<Student> maleStudents = listMapPartitionedByGender.get(true);
        for (Student maleStudent : maleStudents) System.out.println(maleStudent);

        List<Student> femaleStudents = listMapPartitionedByGender.get(false);
        for (Student femaleStudent : femaleStudents) System.out.println(femaleStudent);

        System.out.println("------------------------------------------------------------");
        System.out.println("\n2. 단순분할 + 통계(성별 학생수)");
        // counting() 호출로 반환되는 Collector를 사용
        Map<Boolean, Long> countPartitionedByGender =
                Stream.of(studentArray)
                        .collect(partitioningBy(student -> student.gender.equals(Gender.MALE), counting()));
        System.out.println("남학생 수: " + countPartitionedByGender.get(true));
        System.out.println("여학생 수: " + countPartitionedByGender.get(false));

        System.out.println("------------------------------------------------------------");
        System.out.println("\n3. 단순분할 + 통계(성별 1등)");
        // maxBy() 호출로 반환되는 Collector를 사용
        Map<Boolean, Optional<Student>> topScorePartitionedByGender1 =
                Stream.of(studentArray)
                        .collect(partitioningBy(student -> student.gender.equals(Gender.MALE), maxBy(Comparator.comparingInt(Student::score))));
        System.out.println("남학생 1등: " + topScorePartitionedByGender1.get(true));
        System.out.println("여학생 1등: " + topScorePartitionedByGender1.get(false));

        // Collectors의 collectingAndThen() 사용 - finisher로 Optional의 get() 넘김 -> Optional<Student>가 아니라 Student로 바로 받음
        Map<Boolean, Student> topScorePartitionedByGender2 =
                Stream.of(studentArray)
                        .collect(partitioningBy(student -> student.gender.equals(Gender.MALE), collectingAndThen(maxBy(Comparator.comparingInt(Student::score)), Optional::get)));
        System.out.println("남학생 1등: " + topScorePartitionedByGender2.get(true));
        System.out.println("여학생 1등: " + topScorePartitionedByGender2.get(false));

        System.out.println("------------------------------------------------------------");
        System.out.println("\n4. 다중분할 (성별 불합격자, 100점 이하)");
        // 성적 100점 이하 여부로 먼저 partition -> 성별로 partition -> 값을 꺼내올 때도 get()을 두 번 사용해야함
        Map<Boolean, Map<Boolean, List<Student>>> failedStudentsPartitionedByGender =
                Stream.of(studentArray)
                        .collect(partitioningBy(student -> student.gender.equals(Gender.MALE), partitioningBy(student -> student.score <= 100)));

        List<Student> failedMaleStudentList = failedStudentsPartitionedByGender.get(true).get(true);
        for (Student student : failedMaleStudentList) System.out.println(student);

        List<Student> failedFemaleStudentList = failedStudentsPartitionedByGender.get(false).get(true);
        for (Student student : failedFemaleStudentList) System.out.println(student);

        System.out.println("============================================================");
    }
    // p.849 그룹화와 분할 - partitioningBy(), groupingBy()
    // p.856 예제 14-16 관련
    public void groupingByEx() {
        System.out.println("groupingByEx()");
        System.out.println("groupingBy는 Function을 사용 - 스트림을 둘 이상의 그룹으로 나눠야 하는 경우"); // 당연하겠지만 두 그룹으로 나눌 때도 사용할 수 있다.

        // (1) <T, K> Collector<T, ?, Map<K, List<T>>> groupingBy(Function<? super T, ? extends K> classifier)
        //     -> 아래 (2) 메서드에 downstream으로 Collectors.toList()를 넘긴 것
        // (2) <T, K, A, D> Collector<T, ?, Map<K, D>> groupingBy(Function<? super T, ? extends K> classifier,
        //                                                        Collector<? super T, A, D> downstream)
        //     -> 아래 (3) 메서드에 mapFactory로 HashMap::new를 넘긴 것
        // (3) <T, K, D, A, M extends Map<K, D>>
        //    Collector<T, ?, M> groupingBy(Function<? super T, ? extends K> classifier,
        //                                  Supplier<M> mapFactory,
        //                                  Collector<? super T, A, D> downstream)
        // ==> partitioningBy()와 다른 특징적인 부분이 있다면, (1) Predicate가 아닌 Function classifier로 여러 그룹으로 나누는 것 (2)Supplier<M> mapFactory를 넘기는 것

        System.out.println("1. 단순그룹화()"); // cf. 반으로만 묶었기 때문에 1, 2학년 함께 있음
        // Map<Integer, List<Student>> studentGroupedBySection =
        //         Stream.of(studentArray)
        //                 .collect(groupingBy(Student::section, toList())); // Collectors.toList()는 생략 가능
        Map<Integer, List<Student>> studentGroupedBySection =
                Stream.of(studentArray)
                        .collect(groupingBy(Student::section));
        // -> key는 int section의 Wrapper가 된다.

        for (List<Student> sections : studentGroupedBySection.values()) {
            for (Student student : sections) {
                System.out.println(student);
            }
        }

        System.out.println("------------------------------------------------------------");
        System.out.println("\n2. 단순그룹화(성적별로 그룹화)");
        Map<Level, List<Student>> studentGroupedByLevel =
                Stream.of(studentArray)
                        .collect(groupingBy(s -> s.score >= 200 ? Level.HIGH : s.score >= 100 ? Level.MID : Level.LOW));
        // -> key는 Level이 된다.

        TreeSet<Level> levels = new TreeSet<>(studentGroupedByLevel.keySet());

        for (Level level : levels) {
            System.out.println("[" + level + "]");
            for (Student student : studentGroupedByLevel.get(level)) {
                System.out.println(student);
            }
            System.out.println();
        }

        System.out.println("------------------------------------------------------------");
        System.out.println("\n3. 단순그룹화 + 통계(성적별 학생수)");
        Map<Level, Long> studentCountGroupedByLevel =
                Stream.of(studentArray)
                        .collect(groupingBy(student -> student.score >= 200 ? Level.HIGH : student.score >= 100 ? Level.MID : Level.LOW, counting()));
        // -> key는 Level이 된다.

        for (Level level : studentCountGroupedByLevel.keySet()) {
            System.out.printf("[%s] - %d명, ", level, studentCountGroupedByLevel.get(level));
        }
        System.out.println();
        for (List<Student> level : studentGroupedByLevel.values()) {
            System.out.println();
            for (Student student : level) {
                System.out.println(student);
            }
        }

        System.out.println("------------------------------------------------------------");
        System.out.println("\n4. 다중그룹화 (학년별, 반별)");
        Map<Integer, Map<Integer, List<Student>>> studentGroupedByGradeAndSection =
                Stream.of(studentArray).collect(groupingBy(Student::grade, groupingBy(Student::section)));
        // -> key 학년으로 한 번 그룹화한 후 key 반으로 한 번 더 그룹화

        for (Map<Integer, List<Student>> grade : studentGroupedByGradeAndSection.values()) {
            for (List<Student> section : grade.values()) {
                System.out.println();
                for (Student student : section) {
                    System.out.println(student);
                }
            }
        }

        System.out.println("------------------------------------------------------------");
        System.out.println("\n5. 다중그룹화 + 통계(학년별, 반별 1등)");
        Map<Integer, Map<Integer, Student>> topStudentGroupedByGradeAndSection =
                Stream.of(studentArray)
                        .collect(groupingBy(Student::grade,
                                    groupingBy(Student::section,
                                            collectingAndThen(maxBy(Comparator.comparingInt(Student::score)), Optional::get)))
                        );
        // -> key 학년으로 한 번 그룹화한 후 key 반으로 한 번 더 그룹화 - 그리고 각 그룹마다 추가 작업 scroe 가장 큰 Student get

        for (Map<Integer, Student> grade : topStudentGroupedByGradeAndSection.values()) {
            for (Student student : grade.values()) {
                System.out.println(student);
            }
        }

        System.out.println("------------------------------------------------------------");
        System.out.println("\n6. 다중그룹화 + 통계(학년별, 반별 성적그룹)");
        Map<String, Set<Level>> studentGroupedByScoreGroup = Stream.of(studentArray)
                .collect(groupingBy(student -> student.grade + "-" + student.section,
                            mapping(student -> student.score >= 200 ? Level.HIGH : student.score >= 100 ? Level.MID : Level.LOW,
                                    toSet())));
        // -> key 학년-반 String으로 그룹화한 후, Collectors.mapping() 사용해서 Student score -> Level mapping 후 Set으로 collect
        // --> 학생들을 Level에 따라 그룹으로 나누는 게 아니라, 각 그룹에 속한 학생들의 Level들을 Set으로 가져오는 것이 목적이므로
        //     groupingBy(~)가 아닌 mapping(~, toSet())으로 collect(~) 한 것

        Set<String> levels2 = studentGroupedByScoreGroup.keySet();

        for (String level : levels2) {
            System.out.println("[" + level + "]" + studentGroupedByScoreGroup.get(level));
        }

        System.out.println("============================================================");
    }
    
    // Student 클래스 및 연습용 데이터
    private record Student(int id, String name, Gender gender, int grade, int section, int score) implements Comparable<Student> {

        private static final AtomicInteger count = new AtomicInteger();

        private Student(String name, Gender gender, int grade, int section, int score) {
            this(0, name, gender, grade, section, score);
        }

        private Student(int id, String name, Gender gender, int grade, int section, int score) {
            this.id = count.incrementAndGet();
            this.name = name;
            this.gender = gender;
            this.grade = grade;
            this.section = section;
            this.score = score;
        }

        @Override
        public String toString() {
            return String.format("[%s, %s, %d학년, %d반, %3d점]", name, gender, grade, section, score);
        }

        @Override
        public int compareTo(Student other) {
            return this.score - other.score;
        }
    }

    private enum Gender {
        MALE("남"), FEMALE("여");

        final String gender;

        Gender(String gender) {
            this.gender = gender;
        }

        @Override
        public String toString() {
            return gender;
        }
    }

    enum Level { // groupingBy()에서 사용
        HIGH, MID, LOW; // 성적 상, 중, 하 세 단계 분류
    }

    Student[] studentArray = {
            // 1학년
            new Student("나자바", Gender.MALE, 1, 1, 300),
            new Student("김지미", Gender.FEMALE, 1, 1, 250),
            new Student("나자바", Gender.MALE, 1, 1, 200),
            new Student("이지미", Gender.FEMALE, 1, 2, 150),
            new Student("나자바", Gender.MALE, 1, 2, 100),
            new Student("안지미", Gender.FEMALE, 1, 2, 50),
            new Student("황지미", Gender.FEMALE, 1, 3, 100),
            new Student("강지미", Gender.FEMALE, 1, 3, 150),
            new Student("나자바", Gender.MALE, 1, 3, 200),
            // 2학년
            new Student("나자바", Gender.MALE, 2, 1, 300),
            new Student("김지미", Gender.FEMALE, 2, 1, 250),
            new Student("나자바", Gender.MALE, 2, 1, 200),
            new Student("이지미", Gender.FEMALE, 2, 2, 150),
            new Student("나자바", Gender.MALE, 2, 2, 100),
            new Student("안지미", Gender.FEMALE, 2, 2, 50),
            new Student("황지미", Gender.FEMALE, 2, 3, 100),
            new Student("강지미", Gender.FEMALE, 2, 3, 150),
            new Student("나자바", Gender.MALE, 2, 3, 200)
    };
}
