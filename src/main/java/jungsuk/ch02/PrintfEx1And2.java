package jungsuk.ch02;

// p.36 ~ 39 형식화된 출력 - 형식 지시자(format specifier)

/**
 *    (별도 참고) format string syntax
 *    - Formatter: 클래스 소스 파일 상단 docs 참고
 *    - PrintStream의 printf(~), format(~)
 *    => *** <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html">Formatter docs</a>
 *    => <a href="https://docs.oracle.com/javase/8/docs/api/java/io/PrintStream.html">PrintStream docs</a>
 *    => <a href="https://docs.oracle.com/javase/tutorial/java/data/numberformat.html">Java tutorial - numberformat</a>
 *  ---------------------------------------------------------------------------------------------------------------------
 *    Formatter 상단 설명
 *    The format specifiers for general, character, and numeric types have the following syntax:
 *    %[argument_index$][flags][width][.precision]conversion
 *    - The optional argument_index is a decimal integer indicating the position of the argument in the argument list. The first argument is referenced by "1$", the second by "2$", etc.
 *      - argument_index: 몇 번째 인자인지 지정, format string 다음 인자부터 1, 2, 3, ... 순서
 *    - The optional flags is a set of characters that modify the output format. The set of valid flags depends on the conversion.
 *      - flags: 특정한 형식으로 출력해달라고 지정해주는 기호, 자세한 내용은 docs 중 Flags 항목 참고 => '-', '#', ' ', '0', ',', '('
 *    - The optional width is a positive decimal integer indicating the minimum number of characters to be written to the output.
 *      - width: 출력되는 최소 문자 수(양의 정수)
 *    - The optional precision is a non-negative decimal integer usually used to restrict the number of characters. The specific behavior depends on the conversion.
 *      - precision: 출력되는 최대 문자 수 제한(음이 아닌 정수)
 *    - The required conversion is a character indicating how the argument should be formatted. The set of valid conversions for a given argument depends on the argument's data type.
 *      - conversion: 인자가 어떤 형식으로 포매팅될 것인지 지정 => d, o, x, c, s, ... 등 conversion은 format specifiers의 필수 요소임
 */
public class PrintfEx1And2 {

    public void printfNumbers_byte_short_char() {
        System.out.println("printfNumbers_byte_short_char()");

        byte b = 1;
        short s = 2;
        char c = 'A';

        System.out.printf("b = %d%n", b); // %d: decimal 형식으로 출력
        System.out.printf("s = %d%n", s);
        System.out.printf("c = %c // (int) c = %d(c의 int 값을 출력)%n", c, (int) c);
        System.out.printf("c = %2$c // (int) c = %1$d(argument index를 일부러 바꿔봄)%n", (int) c, 68 /*%2$c로 c로 지정되어 있는데 int를 넣으면 알아서 char로 바뀜*/);
        System.out.println("============================================================");
    }

    public void printfNumbersWithFlagAndWidth() {
        System.out.println("printfNumbersWithFlagAndWidth()");

        int finger = 10;

        System.out.printf("finger = [%5d]%n", finger);  // finger = [   10] // width 5
        System.out.printf("finger = [%-5d]%n", finger); // finger = [10   ] // flag '-': left-justified & width 5
        System.out.printf("finger = [%05d]%n", finger); // finger = [00010] // flag '0': zero-padded & width 5
        System.out.println("============================================================");
    }

    public void printfNumbers_longWithUnderscore() {
        System.out.println("printfNumbers_longWithUnderscore()");

        long big = 100_000_000_000L;

        System.out.printf("big = %d%n", big);
        // 출력 결과: big = 100000000000 -> _까지 출력되진 않음
        System.out.println("============================================================");
    }

    public void printfNumbersWithFlagSharp() {
        System.out.println("printfNumbersWithFlagSharp()");

        long hex = 0xFFFF_FFFF_FFFF_FFFFL;
        int oct = 0123;
        int bin = 0b111;

        System.out.printf("hex = %#x - flag #이 있는 경우%n", hex); // flag # -> 접두사를 표출하게 함 => hexa-decimal은 접두사 0x, octal은 0
        System.out.printf("hex = %#X - flag #이 있는 경우 - conversion을 대문자 X로 준 경우%n", hex);
        System.out.printf("hex = %x - flag #이 없는 경우%n", hex);
        System.out.printf("oct = %#o - flag #이 있는 경우%n", oct);
        System.out.printf("oct = %o - flag #이 없는 경우%n", oct);
        System.out.printf("bin = %d - decimal 형식에서는 flag # 허용되지 않음%n", bin); // %#d에서는 flag # 허용되지 않음(FormatFlagsConversionMismatchException)
        System.out.println("============================================================");
    }

    public void printfNumbersWithVariousConversion() {
        System.out.println("printfNumbersWithVariousConversion()");

        int octNum = 0100;   //  8진수 10, 10진수로는 8
        int hexNum = 0x100;  // 16진수 10, 10진수로는 16
        int binNum = 0b100;  //  2진수 10, 10진수로는 2

        System.out.printf("octNum = %o, %d%n", octNum, octNum);
        // 출력 결과: octNum = 100, 64
        System.out.printf("octNum = %o, %1$d - argument_index를 사용한 결과, 위와 같음%n", octNum);
        // 출력 결과: octNum = 100, 64 - argument_index를 사용한 결과, 위와 같음
        System.out.printf("hexNum = %h, %1$h%n", hexNum);
        // 출력 결과: hexNum = 100, 100
        System.out.printf("binNum = %s, %d - binary 형식 출력은 따로 없음, Integer.toBinaryString(~) 사용%n", Integer.toBinaryString(binNum), binNum);
        // 출력 결과: binNum = 100, 4 - binary 형식 출력은 따로 없음, Integer.toBinaryString(~) 사용
        System.out.println("============================================================");
    }

    public void printfRealNumber_exponentExpression_WidthAndPrecision() {
        System.out.println("printfRealNumber_exponentExpression_precision()");

        float f1 = .10f;
        float f2 = 1e1f;
        float f3 = 3.14e3f;
        System.out.printf("f1 = %f, %1$e, %1$g%n", f1);
        // 출력 결과: f1 = 0.100000, 1.000000e-01, 0.100000
        System.out.printf("f2 = %f, %1$e, %1$g%n", f2);
        // 출력 결과: f2 = 10.000000, 1.000000e+01, 10.0000
        System.out.printf("f3 = %f, %1$e, %1$g%n", f3);
        // 출력 결과: f3 = 3140.000000, 3.140000e+03, 3140.00

        System.out.println();
        double d = 1.23456789;
        // %f는 기본적으로 소수점 아래 6자리까지만 출력(소수점 아래 7자리에서 반올림)
        System.out.printf("d = %f%n", d);
        // 출력 결과: d = 1.234568
        System.out.printf("d = %14.10f%n", d);
        // 출력 결과: d =   1.2345678900
        // %[argument_index$][flags][width][.precision]conversion에서 %[width][.precision]conversion 형태라고 보면 된다.
        System.out.printf("d = %014.10f%n", d); // flag = 0, width = 14, precision = .10
        // 출력 결과: d = 001.2345678900 // 소수점도 한자리를 차지함
        System.out.println("width + precision 관련, FloatEx의 printfFloat()도 확인할 것");

        System.out.println("============================================================");
    }

    public void printfStringWithWidthAndPrecision() {
        System.out.println("printfStringWithWidth()");
        String url = "www.codechobo.com";

        System.out.printf("[12345678901234567890]%n");
        System.out.printf("[%s]%n", url);
        System.out.printf("[%20s]%n", url);
        // 출력 결과: [   www.codechobo.com]
        System.out.printf("[%-20s]%n", url);
        // 출력 결과: [www.codechobo.com   ] -> left-justified
        System.out.printf("[%.8s]%n", url);
        // 출력 결과: [www.code] -> %s conversion에 precision이 있는 경우 앞에서부터 해당 글자만큼 출력
        System.out.println("============================================================");
    }
}
