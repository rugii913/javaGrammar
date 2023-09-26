package jungsuk.ch02;

// p.56 primitive type 중 char - 문자형
public class CharToCodeAndSpecialCharEx {

    public void charToCode() {
        System.out.println("charToCode()");
        char ch = 'A'; // char ch = 65; // => 문자의 유니코드를 직접 변수에 저장할 수도 있다.
        int code = (int) ch;            // ch에 저장된 값을 int로 캐스팅해서 int형 변수에 저장할 수 있다.
        // cf. IDE에서는 <'ch'을(를) 'int'(으)로 형 변환하는 것은 불필요합니다> 라고 잡아줌
        // char(2byte)보다 int(2byte)가 더 범위가 넓은 자료형이므로 자동 형변환(p.83 - p.75 관련)

        System.out.printf("%c = %d (hexa-decimal형으로 출력: %2$#X)%n", ch, code);
        // format specifier 관련 참고
        System.out.printf("%1$c = %1$d (hexa-decimal형으로 출력: %1$#X) // format specifier 관련 연습%n", code);
        // System.out.printf("%1$c = %1$d (hexa-decimal형으로 출력: %1$#X)", ch);
        // IllegalFormatConversionException <- d != java.lang.Character

        System.out.println("------------------------------------------------------------");
        System.out.println("한글 char는?");
        char hch = '가'; // char hch = 0xAC00;
        System.out.printf("%c = %d (hexa-decimal형으로 출력: %2$#X)%n", hch, (int) hch);

        System.out.println("------------------------------------------------------------");
        System.out.println("유니코드를 사용한 변수 저장 가능 ex. 'A' = '\\u0041' = 65, '가' = '\\uAC00' = 0xAC00");
        // IDE에서는 직접 문자로 나타내는 것을 추천함 // u0041 유니코드 사용할 때, 작은 따옴표 꼭 있어야 함
        char charA = '\u0041';
        char charGa = '\uAC00';
        System.out.println(charA);
        System.out.println(charGa);
        System.out.printf("%c, %c, %c %n", 'A', '\u0041', 65);
        System.out.printf("%c, %c, %c %n", '가', '\uAC00', 44032);

        System.out.println("============================================================");
    }

    public void specialCharEx() {
        System.out.println("specialCharEx()");
        System.out.println("특수 문자을 위한 문자 리터럴\n");
        System.out.println('\'');               // '''처럼 쓸수 없으므로, \': 작은따옴표 문자 리터럴를 사용해서 표현해줘야 한다.
        System.out.println("abc\t123\b456");    // \t: 탭, \b: 백스페이스 (여기서는 \b에 의해 3이 지워진다.)
        System.out.println('\n');               // 개행(new line) 문자 출력하고 개행
        System.out.println("\"Hello\"");        // \": 큰따옴표 문자 리터럴
        System.out.println("c:\\");             // \\: 역슬래시 문자 리터럴
        System.out.println("\u0041");           // \ u유니코드: 문자 유니코드 리터럴
        System.out.println("============================================================");
    }
}
