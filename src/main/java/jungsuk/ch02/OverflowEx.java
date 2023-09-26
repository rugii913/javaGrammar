package jungsuk.ch02;

// p.67 오버플로우
// 부호 있는 정수의 overflow vs. 부호 없는 정수의 overflow
public class OverflowEx {

    public void shortOverflow() {
        System.out.println("shortOverflow()");
        short sMin = Short.MIN_VALUE; // short sMin = -32768
        short sMax = Short.MAX_VALUE; // short sMax = 32767

        System.out.println("sMin = " + sMin);                   // sMin = -32768
        System.out.println("sMin - 1 = " + (short) (sMin - 1)); // sMin - 1 = 32767 (양수로 바뀌어버림)
        System.out.println("sMax = " + sMax);                   // sMax = 32767
        System.out.println("sMax + 1 = " + (short) (sMax + 1)); // sMax + 1 = -32768 (음수로 바뀌어버림)

        System.out.println("참고: 애초에 경계를 넘어간 값을 대입할 수 있나?");
        // short sOverflow = 32768; // 필요 타입: short, 제공된 타입: int - 이런 컴파일 에러를 내보냄
        // 참고: short sOverflow = 32767; 여기에는 컴파일 에러를 내지 않음
        short sOverflow = (short) 32768; // 이런 식으로 대입은 가능하다.
        // int 리터럴에서 값을 얻어내고 2바이트 범위를 넘어가는 부분을 잘라버린 후 short 형식으로 읽어낸다고 생각하면 될 듯하다.
        System.out.println("sOverflow = " + sOverflow); // sOverflow = -32768

        System.out.println("------------------------------------------------------------");
        short s = -1;
        short binS = (short) 0b1111_1111_1111_1111;
        System.out.println("참고: 아래 경우에도 0b1111_1111_1111_1111에서 0b0000_0000_0000_0000이 되는데\n" +
                "     해당 타입이 표현할 수 있는 값의 범위를 넘어서는 것이 아니므로 오버플로우라 표현하지 않는다.");
        System.out.println("s = " + s);
        System.out.printf("binS = 0b%s%n", Integer.toBinaryString(s));
        System.out.println("s + 1 = " + (short) (s + 1)); // sMin = -32768
        System.out.printf("binS + 1 = 0b%s%n", Integer.toBinaryString(binS + 1));
        System.out.println("============================================================");
    }

    public void charOverflow() {
        System.out.println("charOverflow()");
        char cMin = Character.MIN_VALUE; // char cMin = 0
        char cMax = Character.MAX_VALUE; // char cMax = 65535

        System.out.println("cMin = " + (int) cMin);         // (int) cMin = 0
        System.out.println("cMin - 1 = " + (int) --cMin);   // (int) --cMin = 65535
        // System.out.println("cMin - 1 = " + ((int) cMin - 1)); // 이렇게 하면 자동으로 int로 연산하여 원하는 현상을 관찰할 수 없음

        System.out.println("cMax = " + (int) cMax);         // (int) cMax = 65535
        System.out.println("cMax + 1= " + (int) ++cMax);    // (int) ++cMax = 0
        // System.out.println("cMax + 1= " + ((int) cMax + 1)); // 이렇게 하면 자동으로 int로 연산하여 원하는 현상을 관찰할 수 없음
        System.out.println("============================================================");
    }
}
