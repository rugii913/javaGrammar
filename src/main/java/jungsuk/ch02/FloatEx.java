package jungsuk.ch02;


// ch02 p.70 float: 실수형 범위, 정밀도, 저장형식, 오차 등
public class FloatEx {

    // p.70 예제 2-10
    public void printfFloat() {
        System.out.println("printfFloat()");
        
        float f1 = 9.12345678901234567890f;
        float f1_1 = 99.12345678901234567890f;
        float f2 = 1.23456789012345678901f;
        double d1 = 9.12345678901234567890d;
        double d2 = 1.23456789012345678901d;
        System.out.println("대입된 값");
        System.out.printf("""
                f1:    %s
                f1_1: %s
                f2:    %s
                d1:    %s
                d2:    %s
                """, "9.12345678901234567890f", "99.12345678901234567890f", "1.23456789012345678901f", "9.12345678901234567890d", "1.23456789012345678901d");
        System.out.println("------------------------------------------------------------");

        System.out.println("         12345678901234567890 (소수점 이하 자릿수 확인)");
        // printf에서 %f conversion의 기본 출력은 소수점 아래 6째 자리까지 출력 - format specifier의 width, precision 지정하지 않았을 경우
        System.out.printf("f1:    %f%n", f1);
        System.out.printf("f1_1: %f%n", f1_1);
        // format specifier에서 width: 24, precision: 20으로 준 경우
        // precision은 소수점 아래 몇 자리까지 나타낼 것인지 - %f의 기본 precision 값이 0이라고 생각하면 됨
        System.out.println("     1234                     (format spcifier의 width 확인)");
        System.out.println("         12345678901234567890 (소수점 이하 자릿수 확인)");
        System.out.printf("f1:  %24.20f%n", f1);
        // => 총 width 24칸을 차지하는 포맷(소수점 합쳐서), 소수점 아래로는 20자리까지 출력
        // System.out.printf("%24.20d%n", 10);
        // => cf. (런타임 에러 IDE 경고 IllegalFormatPrecisionException) 잘못된 서식 문자열 지정자: '%24.20d'에서는 정밀도('.20')가 허용되지 않습니다
        System.out.printf("f1_1:%24.20f%n", f1_1);
        System.out.printf("f2:  %24.20f%n", f2);
        System.out.printf("d1:  %24.20f%n", d1);
        System.out.printf("d2:  %24.20f%n", d2);
        System.out.println("실수형 float의 precision은 7자리, 실수형 double의 precision은 15자리");
        System.out.println("실수형 float의 저장형식: S(1) + E(8) + M(23) / 실수형 double의 저장형식: S(1) + E(11) + M(52)");
        System.out.println("------------------------------------------------------------");
        System.out.println("--참고 printf %f - specifier width, precision 관련--");
        System.out.printf("f1:    %010.7f%n", f1);
        System.out.printf("f1:    %10.8f%n", f1);
        System.out.printf("f1:    %10.9f%n", f1);
        System.out.printf("f1:    %10.10f%n", f1);
        System.out.println("precision 때문에 width를 초과하게 되면, width를 무시하고 precision까지 출력하는 것을 확인할 수 있음");
        System.out.println("============================================================");
    }

    // p.73 예제 2-11
    public void floatToBinaryEx() {
        System.out.println("floatToBinaryEx()");
        System.out.println("실수형 float의 저장형식: S(1) + E(8) + M(23) / 실수형 double의 저장형식: S(1) + E(11) + M(52)");
        System.out.println("=> 2진수로 변환된 실수를 저장할 때는 먼저 '1.xxx * 2^n' 형태로 정규화");
        System.out.println("=>=>  xxx 부분이 가수(mantissa): float 23bit, double 52bit => 실제 값을 저장하는 부분으로 precision과 관련");
        System.out.println("=>=>    n 부분이 지수(exponent): float  8bit, double 11bit => 표현할 수 있는 값의 범위와 관련");
        System.out.println("=>=>  나머지 1bit가 부호 sign bit");
        System.out.println("------------------------------------------------------------");
        System.out.println("============================================================");
        float f = 9.1234567f;
        int i = Float.floatToIntBits(f); // => float 타입의 값이 실제로 어떻게 저장되는지 확인 가능(32bit -> 16진수 숫자 8개)

        System.out.printf("%f%n", f); // 결과: 9.123457
        System.out.printf("%X%n", i); // 결과: 4111F9AE // (p.73) 실제 이진 실수로 변환한 것과 반올림, 오차 관련 참고
        // System.out.printf("%d%n", i); // => 이렇게 출력할 경우 제대로 출력되지 않음
    }
}
