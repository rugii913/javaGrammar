package jungsuk.ch02;

// ch02 p.75 type casting: 형변환 규칙, 실수형 형변환 메커니즘, 자동 형변환
public class CastingEx {

    /*
     * - casting(형변환): 변수나 !리터럴!의 타입을 다른 타입으로 변환하는 것(참고: p.32 리터럴의 타입) - 서로 다른 타입 간의 연산을 수행할 때 많이 사용
     * - ( ) 소괄호 - 캐스트 연산자
     * - 피연산자의 값은 형변환 후에도 아무런 변화가 없다 - 즉 값을 복사해서 가져가기만 함
     * - (boolean을 제외한) primitive type끼리는 서로 캐스팅 가능하다.
     *
     */

    // p.75 예제 2-12
    public void castingDoesNotAffectOperand() {
        System.out.println("castingDoesNotAffectOperand()");

        double d = 85.4;
        int score = (int) d;

        System.out.println("double d = 85.4;");
        System.out.println("int score = (int) d;");
        System.out.println("score = " + score);
        System.out.println("d = " + d); // int score = (int) d;로 d를 형변환한 연산 후에도 원본 d에는 변화가 없음

        System.out.println("============================================================");
    }

    // p.76 예제 2-13
    public void castingBetweenIntegerNumberPrimitiveTypes() {
        System.out.println("castingBetweenIntegerNumberPrimitiveType()");

        System.out.println("- 큰 타입에서 작은 타입으로 변환 시 loss of data 발생");
        System.out.println("- '( )' 연산에 의해 작은 타입의 크기에 맞춰 값을 자름 -> '=' 연산으로 그 잘린 값을 변수의 공간에 저장");
        int i = 10;
        byte b = (byte) i;
        System.out.printf("[int -> byte] i =  %d -> b = %d (값 손실이 없는 경우)%n", i, b);

        i = 300;
        b = (byte) i;
        // b = i; // (컴파일 에러) 필요 타입: byte, 제공된 타입: int // 큰 타입에서 작은 타입으로 변환 시 명시적인 casting 연산 필요
        System.out.printf("[int -> byte] i = %d -> b = %d (값 손실이 있는 경우)%n", i, b);
        System.out.printf("i = [%32s]%n", Integer.toBinaryString(i)); // i = [                       100101100]
        System.out.printf("b = [%32s]%n", Integer.toBinaryString(b)); // b = [                          101100] 2^8 자리 이상으로는 짤린다.
        System.out.println("- 100101100에서 2^9 자리인 1위로는 모두 잘리고, 00101100이 남는다.");

        System.out.println("------------------------------------------------------------");

        System.out.println("- 작은 타입에서 큰 타입으로 변환 시 loss of data 없음, 원래 값이 양수인 경우 빈 공간은 0으로, 음수인 경우 빈 공간은 1로 채워진다.");
        b = 10;
        i = (int) b; // (IDE 검사) 'b'을(를) 'int'(으)로 형 변환하는 것은 불필요합니다 // auto casting 때문 p.82, p.91 - 대입 연산자도 연산자
        System.out.printf("[byte -> int] b = %d -> i = %d%n", b, i);
        System.out.printf("i = [%32s]%n", Integer.toBinaryString(i));

        b = -2;
        i = (int) b; // (IDE 검사) 'b'을(를) 'int'(으)로 형 변환하는 것은 불필요합니다
        System.out.printf("[byte -> int] b = %d -> i = %d%n", b, i);
        System.out.printf("i = [%32s]%n", Integer.toBinaryString(i));

        System.out.println("============================================================");
    }

    // p.78 예제 2-14
    public void castingBetweenRealNumberPrimitiveTypes() {
        System.out.println("castingBetweenRealNumberPrimitiveType()");

        float f1 = 9.1234567f;
        double d1 = 9.1234567;
        double d2 = (double) f1; // (IDE 검사) 'f1'을(를) 'double'(으)로 형 변환하는 것은 불필요합니다
        System.out.println("""
                float f1 = 9.1234567f;
                double d1 = 9.1234567;
                double d2 = (double) f1;
                """);

        System.out.printf("f1 = %20.18f%n", f1);
        System.out.printf("d1 = %20.18f%n", d1);
        System.out.printf("d2 = %20.18f%n", d2);

        System.out.println("- f1과 d1에 각각 리터럴로는 9.1234567로 같아 보이는 값을 줬지만 결과는 다르다.");
        System.out.println("- f1에 저장된 값을 (double)로 형변환 후 d2에 대입해봐도 값은 그대로이다. - 저장할 때 이미 값이 float의 precision에 맞춰졌기 때문(p.79)");

        System.out.println("============================================================");
    }

    // p.81 예제 2-15
    public void castingBetweenIntegerPrimitiveTypeAndRealPrimitiveType() {
        System.out.println("castingBetweenIntegerPrimitiveTypeAndRealPrimitiveType()");

        int     i1 = 91234567; // 8자리의 10진수
        float   f1 = (float) i1; // int를 float로 형변환      // IDE 검사 경고는 안 뜨지만 (float) 생략 가능
        int     i2 = (int) f1; // float를 다시 int로 형변환

        double  d1 = (double) i1; // int를 double로 형변환    // (IDE 검사) 'i1'을(를) 'double'(으)로 형 변환하는 것은 불필요합니다
        int     i3 = (int) d1; // double을 다시 int로 형변환

        float   f2 = 1.666f;
        int     i4 = (int) f2;

        System.out.println("ㅁ 정수형 -> 실수형 -> 정수형 형변환 - precision 관련 문제");
        System.out.printf("i1 = %d%n", i1);
        System.out.printf("f1 = %f i2 = %d%n", f1, i2); // float의 precision이 7자리까지이므로 부정확한 값이 나옴
        System.out.printf("d1 = %f i3 = %d%n", d1, i3); // double의 precision은 15자리이므로 float에서 발생하는 문제가 없음
        System.out.println("ㅁ 실수형 -> 정수형 형변환 - 소수점 이하는 버려진다.");
        System.out.printf("(int) %f = %d%n", f2, i4); //

        System.out.println("============================================================");
    }

    // p.82 예제 없음 + 리터럴 관련 추가(p.33)
    public void automaticTypeCasting() {
        /*
         * 형변환의 규칙(p.83)
         * - boolean을 제외한 나머지 7개의 primitive type은 서로 형변환이 가능하다.
         * - 기본형(primitive type)과 참조형(reference type)은 서로 형변환할 수 없다.
         * - (자동 형변환) 서로 다른 타입의 변수 간의 연산은 형변환을 하는 것이 원칙이지만, 값의 범위가 작은 타입에서 큰 타입으로의 형변환은 생략할 수 있다.
         */
        System.out.println("automaticTypeCasting()");
        System.out.println("대입 연산 시의 자동 형변환");
        float f1 = 1234; // float f = (float) 1234; // 자동 형변환: 형변환의 생략
        System.out.println("float f1 = 1234;");
        System.out.printf("%f%n", f1);
        System.out.println("혼자 참고 - 리터럴 관련");
        float f2 = 123456789f; // 리터럴 float을 대입 // cf. 저장 과정에서 precision 문제로 짤림
        float f3 = 0b111010110111100110100010101; // 리터럴 binary 형태 int 
        // float f3 = 0b111010110111100110100010101f; // (컴파일 오류) ';'이 필요합니다, 심볼 'f'를 해결할 수 없습니다
        float f4 = (float) 0b111010110111100110100010101;
        // float f5 = 0b1.2; // 실수 형태 binary 리터럴은 없음 -> 16진수 나타내는 p 사용
        System.out.printf("%f%n", f2);
        System.out.printf("%f%n", f3);
        System.out.printf("%f%n", f4);
        System.out.println("------------------------------------------------------------");
        // byte b = 128; // (IDE 검사) 필요 타입: byte 제공된 타입: int (컴파일 에러) incompatible types: possible lossy conversion from int to byte
        byte b = (byte) 128; // 명시적으로 형변환하면 의도적인 것으로 간주, 형변환시 overflow로 인한 loss of data 발생(lossy conversion) - 값 손실 발생한 데이터를 변수에 저장함
        System.out.println("byte b = (byte) 128;");
        System.out.printf("%d%n", b);
        System.out.println("------------------------------------------------------------");
        System.out.println("그 외 연산 - 산술 변환 관련 p.91");
        System.out.println("""
                int i = 3;
                double d = 1.0 + i;""");
        int i = 3;
        double d = 1.0 + i;
        System.out.printf("d = %f%n", d);
        System.out.println("============================================================");
    }
}
