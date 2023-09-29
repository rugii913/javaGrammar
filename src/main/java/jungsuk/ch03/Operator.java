package jungsuk.ch03;

// ch03 p.86 operator: 연산자 - 산술 변환, char 연산, 단축 평가, 비트 연산
public class Operator {
    /*
     * p.86 내용 관련 - TODO: 더 명확하게 정리해볼 것
     * operator(연산자): 연산을 수행하는 기호(+, *, (int), =, new ... 등)
     * operand(피연산자): 연산자의 작업 대상(변수 variable, 상수 constant, 리터럴 literal, 식 expression)
     * expression(식): 연산자와 피연산자를 조합하여 계산하고자하는 바를 표현한 것
     * evaluation(평가): 식을 계산하여 결과를 얻는 것
     * statement(문장): ?? - 식의 끝에 ;를 붙여서 문장으로 만든다.
     * 연산 순서 참고: https://docs.oracle.com/javase/specs/jls/se17/html/jls-15.html#jls-15.7
     */
    /*
     * literal
     * - https://docs.oracle.com/javase/specs/jls/se17/html/jls-3.html#jls-3.10
     * - A literal is the source code representation of a value of a primitive type (§4.2), the String type (§4.3.3), or the null type (§4.1).
     *   - 값의 원천이 다른 소스가 아닌 소스 코드 표현이라는 뜻
     */

    // p.91 예제 없음, p.98 예제 3-6 ~ 3-10
    public void conversionInNumericContext() {
        // https://docs.oracle.com/javase/specs/jls/se17/html/jls-5.html#jls-5.6
        // p.91 산술 변환(usual arithmetic conversion) + p.82 자동 형변환 관련
        // p.92 산술 변환이란: 연산 직전에 발생하는 자동 형변환
        // - 두 피연산자의 타입을 같게 일치시킨다. (보다 큰 타입으로 일치)
        // - 피연산자의 타입이 int보다 작은 타입이면 int로 변환된다.
        System.out.println("conversionInArithmeticContext()");

        byte b1 = 10;
        byte b2 = 30;
        // byte b3 = a + b; // (컴파일 에러) incompatible types: possible lossy conversion from int to byte
        byte b3 = (byte) (b1 + b2); // 연산이 존재하므로 a, b 모두 알아서 int로 바뀌고, 결과도 int로 나온다 -> 결과로 나온 int를 byte로 명시적으로 형변환해줘야 한다.

        byte b4 = (byte) (b1 * b2);
        System.out.println(b4); // int 32bit 중 앞의 24자리를 없애고 하위 8자리(1byte)만을 보존 -> 결과: 300(x) 44(o)
        System.out.println("------------------------------------------------------------");

        int i1 = 1_000_000;
        int i2 = 2_000_000;
        int i3 = i1 * i2;
        System.out.println(i3); // -1454759936
        long l1 = i1 * i2; // (IDE 경고) i1 * i2: 정수 곱하기가 long으로 묵시적 형 변환을 합니다
        System.out.println(l1); // -1454759936 => a * b의 32 bit만큼의 결과
        long l2 = (long) (i1 * i2); // (IDE 경고) i1 * i2: 정수 곱하기가 long으로 묵시적 형 변환을 합니다
        System.out.println(l2); // -1454759936
        long l3 = (long) i1 * i2;
        System.out.println(l3); // 2000000000000

        long a = 1_000_000 * 1_000_000;
        long b = 1_000_000 * 1_000_000L;
        System.out.println("a = " + a);
        System.out.println("b = " + b);

        int c = 1_000_000;
        int result1 = c * c / c;
        int result2 = c / c * c;

        System.out.println("int c = 1_000_000");
        System.out.printf("%1$d * %1$d / %1$d = %2$d%n", c, result1); // 1000000 * 1000000 / 1000000 = -727
        System.out.printf("%1$d / %1$d * %1$d = %2$d%n", c, result2); // 1000000 / 1000000 * 1000000 = 1000000

        System.out.println("============================================================");
    }

    // p.102 예제 3-11 ~ 3-16
    public void charOperation() {
        System.out.println("charOperation()");

        char a = 'a';
        char d = 'd';
        char zero = '0';
        char two = '2';

        System.out.printf("'%c' = %d%n", a, (int) a);
        System.out.printf("'%c' = %d%n", d, (int) d);
        System.out.printf("'%c' = %d%n", zero, (int) zero);
        System.out.printf("'%c' = %d%n", two, (int) two);
        System.out.printf("'%c' - '%c' = %d%n", d, a, d - a); // 'd' - 'a' = 3
        System.out.printf("'%c' - '%c' = %d%n", two, zero, two - zero);

        System.out.println("------------------------------------------------------------");

        char c1 = 'a'; // c1에는 문자 'a'의 코드값인 97이 저장된다.
        char c2 = c1; // c1에 저장되어 있는 값이 c2에 저장된다.
        char c3 = ' '; // c3를 공백 문자로 초기화 한다. // (IDE 검사) 가변 'c3' 이니셜라이저 '' ''은(는) 불필요합니다

        int i = c1 + 1; // c1이 int로 산술 변환 'a' + 1 -> 97 + 1 -> 98

        c3 = (char) (c1 + 1); // 연산 시 int로 산술변환 및 그 결과도 int이므로 char형 c3에 저장하기 위해서는 (char)로 형변환 필요
        c2++;
        c2++;

        System.out.println("i = " + i); // 98
        System.out.println("c2 = " + c2); // c
        System.out.println("c3 = " + c3); // b

        System.out.println("------------------------------------------------------------");

        char ch1 = 'a';
        // char ch2 = ch1 + 1; // (컴파일 에러) incompatible types: possible lossy conversion from int to char // (IDE 오류) 호환되지 않는 타입입니다. 발견: 'int', 필요: 'char'
        char ch2 = 'a' + 1;
        // char ch3 = (char) ('a' + 1); // 이것도 가능, 그런데 왜 (char) (~)를 안 써도 가능한 것인가?
        // => 상수 또는 리터럴 간의 연산은 (런타임에 변하지 않으므로) 컴파일 시에 컴파일러가 계산, 런타임에 덧셈 연산이 수행되지 않는다.
        // ==> 즉 컴파일 시 덧셈 연산 결과인 문자 'b'를 바로 변수 ch2에 저장
        System.out.println(ch2);

        System.out.println("------------------------------------------------------------");

        char ch_a = 'a';
        for (int j = 0; j < 26; j++) {
            System.out.print(ch_a++); // 단항 연산자 ++, --는 int로 산술 변환을 시키지 않는다.
        }
        System.out.println();
        char ch_0 = '0';
        for (int j = 0; j < 10; j++) {
            System.out.print(ch_0++);
        }
        System.out.println();

        System.out.println("------------------------------------------------------------");
        System.out.println("대문자로 소문자 얻기: - 32 연산 후 (char) 형변환");
        char lowerCase = 'a';
        char upperCase = (char) (lowerCase - 32);
        System.out.println(upperCase);

        System.out.println("============================================================");
    }

    // p.118 예제 3-26 ~3-31
    public void logicalOperation_bitOperation() {
        System.out.println("logicalOperation_bitOperation()");
        
        System.out.println("short circuit evaluation 확인");
        int a = 5;
        int b = 0;

        boolean shortCircuitEvaluationEx1 = a != 0 || ++b != 0;
        boolean shortCircuitEvaluationEx2 = a == 0 && ++b != 0;

        System.out.printf("a = %d, b = %d%n", a, b);
        System.out.printf("(a != 0 || ++b! = 0) = %b%n", shortCircuitEvaluationEx1);
        System.out.printf("(a == 0 && ++b! = 0) = %b%n", shortCircuitEvaluationEx2);
        System.out.printf("b = %d%n", b);

        System.out.println("------------------------------------------------------------");
        System.out.println("bit operation");
        int x = 0xAB, y = 0xF;

        System.out.printf("x = %#X \t\t\t\t\t%s%n", x, toBinaryString_32Width(x));
        System.out.printf("y = %#X \t\t\t\t\t%s%n", y, toBinaryString_32Width(y));

        System.out.printf("%#X | %#X = %#X \t\t\t%s - OR%n", x, y, x | y, toBinaryString_32Width(x | y));
        System.out.printf("%#X & %#X = %#X \t\t\t%s - AND%n", x, y, x & y, toBinaryString_32Width(x & y));
        System.out.printf("%#X ^ %#X = %#X \t\t\t%s - XOR%n", x, y, x ^ y, toBinaryString_32Width(x ^ y));
        System.out.printf("%#X ^ %#X ^ %#X = %#X \t%s%n", x, y, y, x ^ y ^ y, toBinaryString_32Width(x ^ y ^ y));
        // (IDE 경고) 'x ^ y ^ y'을(를) 'x ^ 0'(으)로 바꿀수 있습니다 - 생각해보면 당연, y ^ y = 0 이기도 하고...

        System.out.println();
        System.out.println("비트 전환 연산자 ~: 1의 보수 연산자");
        byte p = 10;
        byte n = -10;

        System.out.printf("     p =  %d \t%s%n", p, toBinaryString_32Width(p));
        System.out.printf("    ~p = %d \t%s%n", ~p, toBinaryString_32Width(~p));
        System.out.printf("~p + 1 = %d \t%s - 양의 정수 -> 음의 정수 얻기%n", ~p + 1, toBinaryString_32Width(~p + 1));
        System.out.printf("   ~~p =  %d \t%s%n", ~~p, toBinaryString_32Width(~~p));
        System.out.println();
        System.out.printf("     n = %d%n", n);
        System.out.printf("~(n - 1) = %d - 음의 정수 -> 양의 정수 얻기%n", ~(n - 1));

        System.out.println();
        System.out.println("shift operator + bit AND 예제");
        int dec = 1234;
        int hex = 0xABCD;
        int mask = 0xF;

        System.out.printf("hex = %X%n", hex);
        System.out.printf("%X%n", hex & mask);

        hex = hex >> 4;
        System.out.printf("%X%n", hex & mask);

        hex = hex >> 4;
        System.out.printf("%X%n", hex & mask);

        hex = hex >> 4;
        System.out.printf("%X%n", hex & mask);

        System.out.println("============================================================");
    }

    // 위 logicalOperation_bitOperation()에서 사용하는 private 메서드
    private String toBinaryString_32Width(int input) {
        String zero = "00000000000000000000000000000000";
        String tmp = zero + Integer.toBinaryString(input);
        return tmp.substring(tmp.length() - 32);
    }
}
