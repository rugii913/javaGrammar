package jungsuk.ch05;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

// ch04 p.182 array: 배열 기초-범위 초과 예외, 길이 0 배열, 생성+초기화, Arrays.toString, System.arrayCopy
public class ArrayBasic {
    /*
     * array(배열): 같은 타입의 여러 변수를 하나의 묶음으로 다루는 것 - 배열은 각 저장공간이 연속적으로 배치됨
     * 배열의 선언: 생성된 배열을 참조하는 참조변수를 위한 공간 생성 --- ex. 타입[] 변수이름;
     * 배열의 생성: 값을 저장할 수 있는 실제 공간 생성 - 생성 연산자 new와 함께 배열의 타입, 길이를 지정해줘야 함 --- ex. new 타입[길이]
     * element(요소): 생성된 배열의 각 저장공간 - index(인덱스): 배열의 요소마다 붙여진 일련번호 0 ~ (배열길이 - 1)
     * 배열의 길이: 배열의 요소의 개수(값을 저장할 수 있는 공간의 개수) > 0
     *   ==> 길이가 0인 배열도 생성 가능
     *        - ex. p.212 커맨드 라인을 통해 입력받기 관련, main(String[] args) 메서드의 program arguments 관련
     *         ==> JVM은 입력된 program arguments가 없을 때, null 대신 크기가 0인 배열을 생성해서 args에 전달하도록 구현되어 있다.
     */

    // p.186 예제 5-1 관련
    public void arrayBasic_indexOutOfBoundsRuntimeException() {
        System.out.println("arrayBasic_indexOutOfBoundsRuntimeException()");
        System.out.println("ArrayIndexOutOfBoundsException => 런타임 예외");
        int[] arr1 = new int[5];
        try {
            for (int i = 0; i <= 5; i++) {
                arr1[i] = i;
                System.out.printf("%d%n", arr1[i]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // ArrayIndexOutOfBoundsException -> 런타임 에러(컴파일 시에는 잡히지 않음)
            // System.err.println(e);
            // (IDE 경고) 'Throwable'인수 'e'이(가) 'System.err.println()' 호출로 출력됩니다.
            // => 예외 기록을 위해 프린트 문을 사용하면 스택 추적을 숨기기때문에 문제를 조사하는 것이 복잡해질 수 있습니다. 대신 로거를 사용하는 것을 권장합니다.
            // e.printStackTrace();
            // (IDE 경고) 'printStackTrace()'의 호출을 더 강력한 로깅으로 바꿔야 할 것입니다
            // 인수가 없는 Throwable.printStackTrace() 호출을 보고합니다. 그러한 구문은 보통 일시적인 디버그에 자주 사용되기 때문에 프로덕션 코드에서 제거하거나 더욱 강력한 기록 기능으로 바꿔야 합니다.
            Logger logger = Logger.getLogger("logger");
            logger.logp(Level.WARNING, "ArrayEx1", "arrayBasic_indexOutOfBoundsRuntimeException()", "ArrayIndexOutOfBoundsException 예외 발생", e);
        }
        System.out.println("============================================================");
    }

    // p.187 관련
    public void arrayBasic_0length() {
        System.out.println("arrayBasic_0length()");
        System.out.println("길이가 0인 배열도 생성 가능");
        int[] arr2 = new int[0];
        System.out.println("int[] arr2 = new int[0];");
        System.out.printf("arr2.length = %d%n", arr2.length);
        System.out.println("============================================================");
    }

    // p.191 관련
    public void arrayBasic_constructAndInitialization_printViaArraysToString() {
        System.out.println("arrayBasic_constructAndInitialization_printByArraysToString()");
        System.out.println("배열의 생성과 초기화를 동시에 하기 + 배열 쉽게 출력 Arrays.toString(배열 변수명)");
        int[] arr3 = {100, 200, 300, 400, 500}; // int[] arr4 = new int[]{100, 200, 300, 400, 500};라고 표현해도 같다.
        System.out.println("int[] arr3 = {100, 200, 300, 400, 500};");
        System.out.println("Arrays.toString(arr3) = " + Arrays.toString(arr3));

        System.out.println("------------------------------------------------------------");

        System.out.println("- PrintStream.println(char[])의 특별한 overriding");
        char[] charArray = {'a', 'b', 'c', 'd', 'e'}; // int[] arr4 = new int[]{100, 200, 300, 400, 500};라고 표현해도 같다.
        System.out.println("char[] charArray = {'a', 'b', 'c', 'd', 'e'};");
        System.out.println("Arrays.toString(charArray) = " + Arrays.toString(charArray)); // 결과: Arrays.toString(charArray) = [a, b, c, d, e]
        System.out.println("charArray = " + charArray); // 결과: [C@12edcd21 // (IDE 경고) 배열 'charArray'의 'toString()'이 묵시적으로 호출됩니다
        System.out.print("System.out.println(charArray); 출력결과 -> ");
        System.out.println(charArray); // 결과: abcde - println(char[]) 메서드만 조금 특별하게 재정의 되어있음

        System.out.println("============================================================");
    }

    // p.195 예제 5-4
    public void arrayBasic_copy_SystemArrayCopy() {
        // 배열을 복사하는 두 가지 방법 - (1) 반복문 사용 (2) System.arraycopy(~) 사용
        // - for문은 배열의 각 요소에 접근해서 복사하지만, arraycopy()는 지정된 범위의 값들을 통째로 복사하므로 더 빠를 가능성이 높다.
        // - 요소들이 연속적으로 저장되어 있는 배열의 특성 때문
        // arraycopy(~)의 5개 파라미터: Object src, int srcPos, Object dest, int destPos, int length
        char[] abc = {'A', 'B', 'C', 'D'};
        char[] num = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        System.out.println(abc); // 앞서 언급했든 println(char[]) 메서드는 특별하게 재정의 되어있음
        System.out.println(num);

        // 배열 abc와 num을 붙여서 하나의 배열로 만든다. - System.arrayCopy(~) 사용
        char[] result = new char[abc.length + num.length];
        System.arraycopy(abc, 0, result, 0, abc.length);
        System.arraycopy(num, 0, result, abc.length, num.length);
        System.out.println(result);

        // 배열 abc의 index 0부터 abc.length개의 요소를 배열 num의 index 0으로 복사
        System.arraycopy(abc, 0, num, 0, abc.length);
        System.out.println(num);

        // 배열 abc의 index 0부터 3개의 요소를 배열 num의 index 6으로 복사
        System.arraycopy(abc, 0, num, 6, 3);
        System.out.println(num);
    }
}
