package jungsuk.ch14;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/*
* (주로 p.824 관련)
* - Comparator 인터페이스의 메서드들 확인 - 가장 기본이 되는 메서드는 static 메서드는 comparing()
*   - 지원 클래스 Comparators -> Comparators를 구현한 내부 클래스들을 갖고 있음
* - Collections 클래스의 private 내부 클래스 ReverseComparator, ReverseComparator2 확인
* */
public class Stream02_Aux_ComparatorInterface {

    public void comparatorClass() {
        // Comparator<T>를 구현해보면서 어떤 인터페이스인지 알아보기
        // 예를 들어 이 comparator는 절댓값을 비교하는 comparator라고 해보자.
        new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                /*
                * // CodingTest 프로젝트 P11286에서 처음 구현해봤던 형태
                * if (Math.abs(o1) < Math.abs(o2)) {
                *     return -1;
                * } else if (Math.abs(o1) > Math.abs(o2)) {
                *     return 1;
                * } else  {
                *     return o1.compareTo(o2);
                * }
                * */
                // 더 짧은 코드로 구현한 형태
                return Math.abs(o1) == Math.abs(o2) ? o1.compareTo(o2) : Math.abs(o1) - Math.abs(o2);
            }

            /*
            @Override
            public boolean equals(Object obj) {
                return super.equals(obj);
            }
            // 추상 메서드로 정의되어 있지만, override 하지 않아도 된다. 익명 클래스로 만들어질 때, 클래스이므로 기본적으로 Object를 상속하기 때문에 equals(~)까지 알아서 상속해온다.
             */

            // --------------------------------- 여기서부터 default 메서드들 ---------------------------------
            // --------------------------------- ==> Comparator 타입이 인스턴스화되어야 사용 가능
            // --------------------------------- ====> 이미 구현되어있으나 override 가능함
            @Override
            public Comparator<Integer> reversed() {
                /*
                 return Comparator.super.reversed(); // override할 경우 처음 떠있는 코드
                 ==> return Collections.reverseOrder(this); // default 메서드 Comparator<T> reversed()의 구현 코드
                 ====> public static <T> Comparator<T> reverseOrder(Comparator<T> cmp) { // Collections의 static 메서드 reverseOrder(~)
                        // *** cf. ReverseComparator, ReverseComparator2는 모두 Collections의 내부 클래스이고,
                        // ******  ReverseComparator는 Comparator<T>가 Comparator<Comparable<Object>>, 즉 T가 Comparable<Object>이어야만 가능
                        // ******  ReverseComparator2는 T가 Comparable<Object>이 아니어도 가능
                        // *** cf. reverseOrder() 메서드는 ReverseComparator 앞에,
                        // ******  reverseOrder(Comparator<T> cmp) 메서드는 ReverseComparator 뒤, ReverseComparator2 앞으로 두 내부 클래스 사이에 끼어있다.
                        (1) cmp == null일 경우 => reverseOrder() 파라미터 없는 메서드를 호출한 것과 같음
                        if (cmp == null) {
                            return (Comparator<T>) ReverseComparator.REVERSE_ORDER;

                        (2) cmp 인자를 ReverseComparator.REVERSE_ORDER로 준 경우
                        } else if (cmp == ReverseComparator.REVERSE_ORDER) {
                            return (Comparator<T>) Comparators.NaturalOrderComparator.INSTANCE;

                        (3) cmp 인자를 ReverseComparator.NaturalOrderComparator.INSTANCE로 준 경우
                        } else if (cmp == Comparators.NaturalOrderComparator.INSTANCE) {
                            return (Comparator<T>) ReverseComparator.REVERSE_ORDER;

                        (4) cmp 인자로 준 것이 ReverseComparator2 인스턴스인 경우 => 처음에 들어온 cmp를 반환
                        } else if (cmp instanceof ReverseComparator2) {
                            return ((ReverseComparator2<T>) cmp).cmp;

                        (5) 그 밖의 경우 => 파라미터로 받은 cmp를 다시 인자로 넘겨서 새로운 ReverseComparator2를 생성
                        } else {
                            return new ReverseComparator2<>(cmp);
                        }
                 }
                 */
                return Collections.reverseOrder(this);
            }

            /*
            * Comparator 타입 객체가 thenComparing(other)을 호출하면, 새로운 Comparator를 반환
            *   ==> 새로 반환되는 Comparator는 this.compare(c1, c2)로 먼저 비교한 뒤,
            *       this.compare(c1, c2)의 결과가 0인 경우에만, other.compare(c1, c2)로 다시 비교하여 그 값을 반환함.
            *       (마치 Function의 default 메서드 andThen(after)에 의한 합성과 비슷한 느낌)
            *   ==> a.thenComparing(b.thenComparing(c)) - b로 비교했을 때도 0이 나왔을 때, c로 또 비교하도록 할 수도 있을 것
            * */
            // *사용 예시 p.826 예제 참고
            @Override
            public Comparator<Integer> thenComparing(Comparator<? super Integer> other) {
                // return Comparator.super.thenComparing(other); // default 메서드
                Objects.requireNonNull(other);
                return (Comparator<Integer> & Serializable) (c1, c2) -> {
                    int res = compare(c1, c2);
                    return (res != 0) ? res : other.compare(c1, c2);
                };
            }

            // 위 default 메서드 thenComparing(Comparator<? super Integer> other)과 함께, 아래 static 메서드 <T, U> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor, Comparator<? super U> keyComparator) 참고
            // 어떤 Comparator 객체가 thenComparing(keyExtractor, keyComparator)를 호출하면,
            // comparing(keyExtractor, keyComparator)로 만들어지는 Comparator 로직을
            // thenComparing으로 추가시킨 Comparator 인스턴스를 반환
            @Override
            public <U> Comparator<Integer> thenComparing(Function<? super Integer, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
                // return Comparator.super.thenComparing(keyExtractor, keyComparator); // default 메서드
                return thenComparing(comparing(keyExtractor, keyComparator));
            }

            // 위와 같은데 keyExtractor만 있는 형태, 단 U extends Comparable이어야만 한다.
            @Override
            public <U extends Comparable<? super U>> Comparator<Integer> thenComparing(Function<? super Integer, ? extends U> keyExtractor) {
                // return Comparator.super.thenComparing(keyExtractor); // default 메서드
                return thenComparing(comparing(keyExtractor));
            }

            // keyExtractor만 있는 형태, comparingInt로 비교할 것이므로 keyExtractor로 ToIntFunction 필요
            @Override
            public Comparator<Integer> thenComparingInt(ToIntFunction<? super Integer> keyExtractor) {
                // return Comparator.super.thenComparingInt(keyExtractor); // default 메서드
                return thenComparing(comparingInt(keyExtractor)); // static 메서드 호출
            }

            // 위와 거의 같음
            @Override
            public Comparator<Integer> thenComparingLong(ToLongFunction<? super Integer> keyExtractor) {
                // return Comparator.super.thenComparingLong(keyExtractor); // default 메서드
                return thenComparing(comparingLong(keyExtractor)); // static 메서드 호출
            }

            // 위와 거의 같음
            @Override
            public Comparator<Integer> thenComparingDouble(ToDoubleFunction<? super Integer> keyExtractor) {
                // return Comparator.super.thenComparingDouble(keyExtractor); // default 메서드
                return thenComparing(comparingDouble(keyExtractor)); // static 메서드 호출
            }

            // --------------------------------- 여기서부터 public static 메서드들 ---------------------------------
            // --------------------------------- ==> static이므로 Comparator 타입을 인스턴스화 시키지 않고 사용 가능
            // --------------------------------- ====> static이므로 override 불가
            public static <T extends Comparable<? super T>> Comparator<T> reverseOrder() {
                // return Comparator.reverseOrder() -> Comparator.reverseOrder() 하면 아래의 Collections.reverseOrder()로 연결
                return Collections.reverseOrder();
                // return (Comparator<T>) ReverseComparator.REVERSE_ORDER;
            }

            /*
            * 아래 세 메서드 naturalOrder(), nullsFirst(Comparator<? super T> comparator), nullsLast(Comparator<? super T> comparator)는
            * 지원 클래스 java.util.Comparators의 Comparartor를 구현한 내부 클래스들을 사용한다.
            * */
            public static <T extends Comparable<? super T>> Comparator<T> naturalOrder() {
                return Comparator.naturalOrder();
                // return (Comparator<T>) Comparators.NaturalOrderComparator.INSTANCE;
                // => Comparator는 인터페이스이므로 이를 지원하기 위해 java.util.Comparators라는 package private 지원 클래스를 사용함: Package private supporting class for Comparator.
            }

            public static <T> Comparator<T> nullsFirst(Comparator<? super T> comparator) {
                return Comparator.nullsFirst(comparator);
                // return new Comparators.NullComparator<>(true, comparator);
                // => 역시 java.util.Comparators라는 package private 지원 클래스를 사용
            }

            public static <T> Comparator<T> nullsLast(Comparator<? super T> comparator) {
                return Comparator.nullsLast(comparator);
                // return new Comparators.NullComparator<>(false, comparator);
                // => 역시 java.util.Comparators라는 package private 지원 클래스를 사용
            }

            // 매개변수로 Function keyExtractor, Comparator keyComparator 모두 있는 static 메서드
            // Function keyExtractor: ? super T 타입 c1, c2에서 ? extends U 타입 값을 꺼내는 Function - ex. 어떤 객체에서 특정 필드를 추출
            // Comparator keyComparator: ? super U 타입 끼리 compare
            public static <T, U> Comparator<T> comparing(
                    Function<? super T, ? extends U> keyExtractor,
                    Comparator<? super U> keyComparator)
            {
                Objects.requireNonNull(keyExtractor);
                Objects.requireNonNull(keyComparator);
                return (Comparator<T> & Serializable)
                        (c1, c2) -> keyComparator.compare(keyExtractor.apply(c1), keyExtractor.apply(c2));
            }

            // 매개변수로 Function keyExtractor만 있는 static 메서드
            // 대신 U는 Comparable만 가능 ==> Function keyExtractor의 apply(~)의 반환 타입이 Comparable만 가능하다.
            //                          ====> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2)) 이런 식으로 바로 compareTo(~)로 연결이 가능함
            // *사용 예시 p.826 예제 참고
            public static <T, U extends Comparable<? super U>> Comparator<T> comparing(
                    Function<? super T, ? extends U> keyExtractor)
            {
                Objects.requireNonNull(keyExtractor);
                return (Comparator<T> & Serializable)
                        (c1, c2) -> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));
            }

            // comparing과 유사한데 매개변수 중
            // Function keyExtractor 대신 ToIntFunction keyExtractor: 반환 타입이 int인 applyAsInt(~) - ex. 어떤 객체에서 int 타입 필드를 추출
            // keyExtractor의 반환 타입이 int로 정해져있으므로, 자연스럽게 keyComparator는 따로 없이 Integer.compare(~)를 사용함 
            public static <T> Comparator<T> comparingInt(ToIntFunction<? super T> keyExtractor) {
                Objects.requireNonNull(keyExtractor);
                return (Comparator<T> & Serializable)
                        (c1, c2) -> Integer.compare(keyExtractor.applyAsInt(c1), keyExtractor.applyAsInt(c2));
            }

            // comparingInt(~)와 유사
            public static <T> Comparator<T> comparingLong(ToLongFunction<? super T> keyExtractor) {
                Objects.requireNonNull(keyExtractor);
                return (Comparator<T> & Serializable)
                        (c1, c2) -> Long.compare(keyExtractor.applyAsLong(c1), keyExtractor.applyAsLong(c2));
            }

            // comparingInt(~)와 유사
            public static<T> Comparator<T> comparingDouble(ToDoubleFunction<? super T> keyExtractor) {
                Objects.requireNonNull(keyExtractor);
                return (Comparator<T> & Serializable)
                        (c1, c2) -> Double.compare(keyExtractor.applyAsDouble(c1), keyExtractor.applyAsDouble(c2));
            }
        };

        System.out.println("============================================================");
    }

    public void comparatorInCollections() {
        // Collections (cf. Collection<T>와는 다르다.)
        // Collection은 The root interface in the collection hierarchy.
        // Collections는 This class consists exclusively of static methods that operate on or return collections.
        //               -> 생성자는 private으로 생성하지 못하게 해두었고, 메서드들은 모두 static 메서드임

        // Collections는 2개의 내부 클래스 Comparator를 갖고 있다.
        // 1. ReverseComparator
        // 2. ReverseComparator2

        /*
        * 1. ReverseComparator 관련
        * public static <T> Comparator<T> reverseOrder() {
        * return (Comparator<T>) ReverseComparator.REVERSE_ORDER;
        * }
        * ==>
        * public int compare(Comparable<Object> c1, Comparable<Object> c2) {
        *    return c2.compareTo(c1);
        * }
        * */
        Comparator<Object> reverseComparatorInstance_1 = Collections.reverseOrder();

        /*
        * 2. ReverseComparator2 관련
        * public static <T> Comparator<T> reverseOrder(Comparator<T> cmp) {
        *   if (cmp == null) {
        *       return (Comparator<T>) ReverseComparator.REVERSE_ORDER;
        *   } else if (cmp == ReverseComparator.REVERSE_ORDER) {
        *       return (Comparator<T>) Comparators.NaturalOrderComparator.INSTANCE;
        *   } else if (cmp == Comparators.NaturalOrderComparator.INSTANCE) {
        *       return (Comparator<T>) ReverseComparator.REVERSE_ORDER;
        *   } else if (cmp instanceof ReverseComparator2) {
        *       return ((ReverseComparator2<T>) cmp).cmp; // => ReverseComparator2는 원래의 Comparator cmp를 필드로 갖고 있다.
        *   } else {
        *       return new ReverseComparator2<>(cmp);
        *   }
        * }
        * */
        Comparator<Object> reverseComparatorInstance_2 = Collections.reverseOrder(null);
        Comparator<Object> naturalOrderComparatorInstance = Collections.reverseOrder(reverseComparatorInstance_2);
        Comparator<Object> reverseComparatorInstance_3 = Collections.reverseOrder(naturalOrderComparatorInstance);

        // 예를 들어, cmp로 Comparator.comparing(Object::toString)를 넘긴다고 할 때
        Comparator<Object> reverseComparator2Instance = Collections.reverseOrder(Comparator.comparing(Object::toString));
        Comparator<Object> originalComparatorInstance = Collections.reverseOrder(reverseComparator2Instance);
    }
}
