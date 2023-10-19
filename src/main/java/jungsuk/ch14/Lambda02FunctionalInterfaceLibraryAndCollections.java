package jungsuk.ch14;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;

// ch.14 p.802 java.util.function 패키지 - 함수형 인터페이스 + 이를 arg로 받는 Java 컬렉션 프레임워크의 메서드들
public class Lambda02FunctionalInterfaceLibraryAndCollections {
    /*
     * (p.803)
     * Runnable void run() 매개변수 x 반환값 x // cf. 이것만 java.lang 패키지, 나머지는 java.util.function 패키지
     * Supplier<T> T get() 매개변수 x 반환값 o
     * Consumer<T> void accept(T t) 매개변수 1 반환값 o
     * Function<T, R> R apply(T t) 매개변수 1 반환값 o
     * Predicate<T> boolean test(T t) 매개변수 1 반환타입 boolean  *조건식을 람다식으로 표현
     *
     * BiConsumer<T, U> void accept(T t, U u) 매개변수 2 반환값 o
     * BiPredicate<T, U> boolean test(T t, U u) 매개변수 2 반환타입 boolean 1
     * BiFunction<T, U, R> R apply(T t, U u) 매개변수 2 반환값 o
     * cf. 메서드가 두 개의 값을 반환할 수는 없으므로 BiSupplier는 없음
     * cf. 3개 이상의 매개변수를 갖는 functional interface는 직접 만들어서 써야함
     *
     * UnaryOperator<T> T apply(T t) Function의 자손, 매개변수의 타입과 반환타입이 같음
     * BinaryOperator<T> T apply(T t, T t) BiFunction의 자손, 매개변수 둘 모두의 타입과 반환타입이 같음
     *
     * ㅁ 기본형을 사용하는 functional interface (p.806)
     *  ㅇ AToBFunction은 매개변수 타입이 A, 반환타입이 B
     *    ex. DoubleToIntFunction int applyAsInt(double d) // cf. IntToIntFunction 같은 이름은 없고, IntUnaryOperator를 사용하면 된다.
     *  ㅇ ToBFucntion은 매개변수 타입은 generic type, 반환타입이 B
     *    ex. ToIntFunction<T> int applyAsInt(T t)
     *  ㅇ AFunction은 매개변수 타입이 A, 반환타입이 generic type
     *    ex. IntFunction<R> R apply(T t, U u)
     *  ㅇ ObjAFunction은 매개변수 타입이 두 개(generic type, A타입), 반환값은 없음
     *    ex. ObjIntConsumer<T> void accept(T t, int value)
     * */

    // p.805 예제 14-4 관련
    public void collectionsAndFunctionalInterface() {
        /*
         * functional interface를 인자로 받는 메서드들 ex. Collection removeIf(Predicate), ...
         * */
        System.out.println("collectionsAndFunctionalInterface()");
        // -----List 예시-----
        List<Integer> list = new ArrayList<>(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        // list의 모든 요소를 출력
        list.forEach(i -> System.out.print(i + "  "));
        System.out.println();
        // list의 각 요소에 10을 곱한다.
        list.replaceAll(i -> i * 10);
        System.out.println(list);

        // -----Map 예시-----
        Map<Integer, String> map = Map.of(0, "1번", 1, "2번", 2, "3번", 3, "4번");
        // map의 모든 요소를 {k, v} 형식으로 출력한다.
        map.forEach((key, value) -> System.out.printf(("{%d, %s}  "), key, value));
        System.out.println();
        System.out.println("============================================================");
    }

    // p.803, p.806 예제 14-5, 6 관련
    public void functionalInterfaceLibrary() {
        System.out.println("functionalInterfaceLibrary()");

        IntSupplier randomIntSupplier = () -> (int) (Math.random() * 100) + 1;
        IntConsumer printConsumer = i -> System.out.printf("%d  ", i);
        IntPredicate parityPredicate = i -> i % 2 == 0;
        IntUnaryOperator deleteDigitOfOneOperator = i -> i / 10 * 10; // i의 일의 자리를 없애준다.
        // cf. 만약 위에서 IntUnaryOperator 대신 Function을 사용한다면 타입을 알 수 없으므로 에러 발생함

        int[] arr = new int[10];

        makeRandomList(randomIntSupplier, arr);
        System.out.println(Arrays.toString(arr));
        printEvenNumber(parityPredicate, printConsumer, arr);
        int[] newArr = deleteDigitOfOne(deleteDigitOfOneOperator, arr);
        System.out.println(Arrays.toString(newArr));

        System.out.println("============================================================");
    }

    // functionalInterfaceLibrary() 관련 메서드들
    private void makeRandomList(IntSupplier s, int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = s.getAsInt(); // IntSupplier의 메서드 이름은 get()이 아니라 getAsInt()임에 주의
        }
    }

    private void printEvenNumber(IntPredicate p, IntConsumer c, int[] arr) {
        System.out.print("[");
        for (int i : arr) {
            if (p.test(i)) {
                c.accept(i);
            }
        }
        System.out.println("]");
    }

    private int[] deleteDigitOfOne(IntUnaryOperator op, int[] arr) {
        int[] newArr = new int[arr.length];
        for (int i = 0; i < newArr.length; i++) {
            newArr[i] = op.applyAsInt(arr[i]); // IntUnaryOperator의 메서드 이름은 apply(~)가 아니라 applyAsInt(~)임에 유의
        }
        return newArr;
    }
}
